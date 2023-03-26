package com.ipasoft.hazelcast.service.hazelcast;

import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.ipasoft.hazelcast.model.entity.hazelcast.CachedData;
import com.ipasoft.hazelcast.model.entity.redis.Root;

@Service
public class HazelcastDistributedCacheService implements IHazelcastDistributedCacheService {

    private final HazelcastInstance hazelcastInstance;
	
	public HazelcastDistributedCacheService(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
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
			
			IMap<String, String> map = hazelcastInstance.getMap(MY_DISTRIBUTED_MAP);
	        map.put("1", "Hello");
	        map.put("2", "World");
	        return "Map size: " + map.size() + "\n" +
	                "Value for key '1': " + map.get("1") + "\n" +
	                "Value for key '2': " + map.get("2");
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
        IMap<String, String> map = hazelcastInstance.getMap(MY_DISTRIBUTED_MAP);
        return map.get(key);
    }
	@Override
	public void putCachedString(String key, String value) {
        IMap<String, String> map = hazelcastInstance.getMap(MY_DISTRIBUTED_MAP);
        map.put(key, value);
    }

//Test CachedData (for roots) ----------------------------------------------------------------------------------
	@Override
	public CachedData<?> getCachedData(int key) {
		IMap<Integer, CachedData<?>> hazelcastMap = hazelcastInstance.getMap(MY_DISTRIBUTED_MAP);
		return hazelcastMap.get(key);
	}
	@Override
	public CachedData<?> putCachedData(CachedData<?> data) {
		Object o = hazelcastInstance.getMap(MY_DISTRIBUTED_MAP).put(data.getId(), data);
		return (CachedData<?>)o;
	}
	@Override
	public Root putRoot(Root root) {
		CachedData<?> c = this.putCachedData(
				CachedData.<Root>builder().id( root.getId() )
			  							  .value( root )
			  							  .build()
			);
		return 	c == null ? root : (Root)c.getValue();
	}
	@Override
	public Map<Object, Object> getAllCachedData() {
		return hazelcastInstance.getMap(MY_DISTRIBUTED_MAP).getAll(null);
	}
}