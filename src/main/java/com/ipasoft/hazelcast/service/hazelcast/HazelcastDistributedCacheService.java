package com.ipasoft.hazelcast.service.hazelcast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.hazelcast.config.IndexConfig;
import com.hazelcast.config.IndexType;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.projection.Projection;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import com.hazelcast.query.QueryException;
import com.hazelcast.sql.SqlResult;
import com.hazelcast.sql.SqlRow;
import com.ipasoft.hazelcast.model.entity.redis.OtherObject;
import com.ipasoft.hazelcast.model.entity.redis.Root;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HazelcastDistributedCacheService implements IHazelcastDistributedCacheService {

    private final HazelcastInstance hazelcastInstance;
    private final IMap<String, String> stringMap;
    private final IMap<Integer,Root> rootsMap;
    private final IMap<Integer,OtherObject> otherObjectsMap;

	public HazelcastDistributedCacheService(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
		this.stringMap = this.hazelcastInstance.getMap(DISTRIBUTED_STRING_MAP);
		this.rootsMap = this.hazelcastInstance.getMap(DISTRIBUTED_ROOT_MAPS);
		this.otherObjectsMap = this.hazelcastInstance.getMap(DISTRIBUTED_OTHEROBJECT_MAPS);
		this.initializeRootsMapIndexes();
	}

	private void initializeRootsMapIndexes() {
		//creo índices en las propiedades de los objetos almacenados en el mapa
		this.rootsMap.addIndex(IndexType.SORTED, "id");
		this.rootsMap.addIndex(new IndexConfig(IndexType.SORTED, "level1.level2.level3.prop5"));
		this.rootsMap.addIndex(IndexType.SORTED, "level1.prop1");
		this.rootsMap.addIndex(IndexType.SORTED, "level1.prop2");
		
		this.otherObjectsMap.addIndex(IndexType.SORTED, "id");
	}

//Test Cacheable -----------------------------------------------------------------------------------
	/**
	 * Esto hace que se llame solo 1 vez y luego queda cacheado en hacelcast si esta configurado, sino lo deja cacheado en un ConcurrentHashMap
	 */
	@Override
	@Cacheable("ipa-hazel")
	public String getHazelcastDemoMethodCache() {
		return doSomethingThatTakesTooLongToGet();
	}
	/**
	 * sleep 3 seconds and returns data from hazelcast
	 */
	private String doSomethingThatTakesTooLongToGet() {
		String message = "";
		try {
			System.out.println("sleeping...");
			Thread.sleep(3000);
			System.out.println("waked up...");

			this.stringMap.put("1", "Hello");
			this.stringMap.put("2", "World");
	        return "Map size: " + this.stringMap.size() + "\n" +
	                "Value for key '1': " + this.stringMap.get("1") + "\n" +
	                "Value for key '2': " + this.stringMap.get("2");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			message = "ended";
		}
		return message;
    }

// Test Cache String key/value ----------------------------------------------------------------------------------
	@Override
	public String getCachedString(String key) {
        return this.stringMap.get(key);
    }
	@Override
	public void putCachedString(String key, String value) {
        this.stringMap.put(key, value);
    }

//Test CachedData (for roots) ----------------------------------------------------------------------------------
	@Override
	public Root getCachedData(int key) {
		log.info("# Hazelcast Partitions: {}", this.hazelcastInstance.getPartitionService().getPartitions().size());
		return this.rootsMap.get(key);
	}
	@Override
	public Root putRoot(Root root) {
		return this.putCachedData(root);
	}
	@Override
	public Root putCachedData(Root root) {
		try {
			Object o = this.rootsMap.put(root.getId(), root);
			return (Root)o;
		}
		catch (QueryException e) {
			e.printStackTrace();
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	public void putOtherObject(OtherObject otherObject) {
		try {
			this.otherObjectsMap.put(otherObject.getId(), otherObject);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public Map<Integer, Root> getAllCachedData(Integer... keys) {
		return this.rootsMap.getAll( Set.of(keys) );
	}

	private void join(String prop2) {
/*
	    Pipeline pipeline = Pipeline.create();
	    
	    // Crear las dos fuentes de datos
	    BatchSource<Entry<Integer, Root>> finiteJetSource1 = Sources.map(this.rootsMap);
	    BatchSource<Entry<Integer, Root>> finiteJetSource2 = Sources.map(this.rootsMap);

	    // Crear la función de join
	    JoinClause<String,//type of the join key
	    		   Root,//type of the left map stream
	    		   Root,//type of the right map stream
	    		   CachedData<Root> //type of the return type: maybe a sum of left type + right type 
	    		  > joinClause = JoinClause.joinMapEntries(
									                    Root::getId
										            )
										           .onKeys(
										        		 new FunctionEx<String,String>() {
										       				@Override
										       				public String applyEx(String key) throws Exception {
										       					return null;
										       				}
										       			}, 
										       			new FunctionEx<Integer,Root>() {
										       				@Override
										       				public Root applyEx(Integer key) throws Exception {
										       					return null;
										       				}
										       			}
										        	)
										           .projecting(
											        	new FunctionEx<Integer,Root>() {
										       				@Override
										       				public Root applyEx(Integer key) throws Exception {
										       					return null;
										       				}
										       			}
										           );
										           //.filter((objeto1, objeto2) -> objeto2.getValue().getProp2().equals(prop2))
										           //.map((objeto1, objeto2) -> new Object[]{objeto1, objeto2});
	    // Unir las dos fuentes de datos
	    pipeline.readFrom( finiteJetSource1 )
	            .hashJoin( finiteJetSource2, joinClause )
	            .drainTo(  Sinks.list("THIS_JOIN_RESULTS") );

	    // Ejecutar el pipeline
	    JobConfig config = new JobConfig();
	    config.setName("join-job");
	    JetInstance jet = HazelcastJet.newHazelcastJetInstance();
	    Job job = jet.newJob(pipeline, config);
	    List<Object[]> resultados = jet.getList("THIS_JOIN_RESULTS");

	    // Detener la instancia de Jet
	    jet.shutdown();

	    return null;
*/
	}

	private void getJoinSearch() {
/*		Predicate<String,String> stringMapPredicate = Predicates.<String,String>alwaysTrue();
		Predicate<Integer,Root> rootMapPredicate = Predicates.<Integer,Root>ilike("level1.prop1","%rodri%");

		StreamSource<Map.Entry<String, String>> sourceA = Sources.map(DISTRIBUTED_STRING_MAP);
		StreamSource<Map.Entry<Integer, Root>> sourceB = Sources.map(this.rootsMap);
		
		Pipeline pipeline = Pipeline.create();
		
		JoinClause.onKeys((FunctionEx<String, String>) entry -> entry),);
		//this.rootsMapjoi( rootMapPredicate ).join
		
		JoinClause.onKeys(
			new FunctionEx<String,String>() {
				@Override
				public String applyEx(String key) throws Exception {
					return null;
				}
			}, 
			new FunctionEx<Integer,Root>() {
				@Override
				public Root applyEx(Integer key) throws Exception {
					return null;
				}
			}
		);
		JoinClause.joinMapEntries( new FunctionEx<T, K>() {});
*/
	}
	
	/**
	 * aca un ejemplo donde se puede hacer consutlas con sql. No funciona un join con mapas distribuidos.
	 * 
	 */
	private void testSQL() {
		try (SqlResult result = this.hazelcastInstance.getSql().execute("SELECT * FROM root_maps")) {
	         for (SqlRow row : result) {
	             log.info("row {}", row);
	         }
	    }
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Iterable<Root> getFilteredRoots() {
		//testSQL();

		//Realiza la consulta utilizando los índices creados
		Predicate<Integer,Root> rodriPredicate = Predicates.<Integer,Root>ilike("level1.prop1","%rodri%");
		Predicate<Integer,Root> capoPredicate = Predicates.<Integer,Root>ilike("level1.prop2","%capo%");
		//Predicate<Integer,Root> capoPredicate = Predicates.<Integer,Root>.equal("property1", "value1");
		//Predicate<Integer,Root> capoPredicate = Predicates.<Integer,Root>.greaterThan("property2", 100);
		
		Predicate<Integer,Root> finalPredicate = Predicates.or(rodriPredicate, capoPredicate);

		Collection<Root> results = this.rootsMap.values( finalPredicate );//obtiene todos los roots que cumplen el filtro del predicado
		
		return results;//.toList();
	}
	
	@Override
	public Iterable<OtherObject> getJoinedRootsAndOtherObjects() {
		//Realiza la consulta utilizando los índices creados
		Predicate<Integer,Root> rodriPredicate = Predicates.<Integer,Root>ilike("level1.prop1","%rodri%");
		Predicate<Integer,Root> capoPredicate = Predicates.<Integer,Root>ilike("level1.prop2","%capo%");
		Predicate<Integer,Root> orPredicate = Predicates.or(rodriPredicate, capoPredicate);

		Projection<Entry<Integer,Root>, Comparable<Integer>> myProjectionOfIds = entry -> entry.getValue().getId(); //La proyección que obtiene el atributo deseado, en este caso id

		Collection<Comparable<Integer>> IdsOfOtherObjectToFilter = this.rootsMap.project(myProjectionOfIds, orPredicate);

		Predicate<Integer,OtherObject> inFkPredicate = Predicates.in("id", IdsOfOtherObjectToFilter.toArray(new Comparable[IdsOfOtherObjectToFilter.size()]));
		
		Collection<OtherObject> joinedResults = this.otherObjectsMap.values( inFkPredicate );
		
		log.info("resultados filtrados {}", joinedResults);
		
		return joinedResults;
	}
}