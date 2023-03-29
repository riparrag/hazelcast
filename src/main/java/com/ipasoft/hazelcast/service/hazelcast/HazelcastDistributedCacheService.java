package com.ipasoft.hazelcast.service.hazelcast;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.hazelcast.config.IndexConfig;
import com.hazelcast.config.IndexType;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import com.hazelcast.query.QueryException;
import com.ipasoft.hazelcast.model.entity.redis.Root;

@Service
public class HazelcastDistributedCacheService implements IHazelcastDistributedCacheService {

    private final HazelcastInstance hazelcastInstance;
    private final IMap<String, String> stringMap;
    private final IMap<Integer,Root> rootsMap;

	public HazelcastDistributedCacheService(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
		this.stringMap = this.hazelcastInstance.getMap(DISTRIBUTED_STRING_MAP);
		this.rootsMap = this.hazelcastInstance.getMap(DISTRIBUTED_ROOT_MAPS);
		this.initializeRootsMapIndexes();
	}

	private void initializeRootsMapIndexes() {
		//creo índices en las propiedades de los objetos almacenados en el mapa
		this.rootsMap.addIndex(new IndexConfig(IndexType.SORTED, "level1.level2.level3.prop5"));  
		this.rootsMap.addIndex(IndexType.SORTED, "level1.prop1");
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
	@Override
	public Map<Integer, Root> getAllCachedData(Integer... keys) {
		return this.rootsMap.getAll( Set.of(keys) );
	}

	@Override
	public Iterable<Root> getFilteredRoots() {
		// Realizar la consulta utilizando los índices creados
		Predicate<Integer,Root> rodriPredicate = Predicates.<Integer,Root>ilike("level1.prop1","%rodri%");
		Predicate<Integer,Root> capoPredicate = Predicates.<Integer,Root>ilike("level1.prop2","capo%");
		//Predicate<CachedData<Root>> property1Predicate = Predicates.equal("property1", "value1");
		//Predicate<CachedData<Root>> property2Predicate = Predicates.greaterThan("property2", 100);
		Predicate<Integer,Root> finalPredicate = Predicates.and(rodriPredicate, capoPredicate);

		Collection<Root> results = this.rootsMap.values( finalPredicate );

		return results;//.toList();
	}
}