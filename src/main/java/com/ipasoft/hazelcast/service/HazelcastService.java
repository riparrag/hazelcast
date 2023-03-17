package com.ipasoft.hazelcast.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

//import com.ipasoft.hazelcast.controller.HazelcastInstance;
//import com.ipasoft.hazelcast.controller.IMap;

@Service
public class HazelcastService implements IHazelcastService {

	@Autowired
    private HazelcastInstance hazelcastInstance;

	@Cacheable("ipa-hazel")
	@Override
	/**
	 * Esto hace que se llame solo 1 vez y luego queda cacheado en hacelcast si esta configurado, sino lo deja cacheado en un ConcurrentHashMap
	 */
	public String getHazelcastDemo() {
		return doSomethingToGet();
	}
	
	@Override
    public void put(String key, String value) {
        IMap<String, String> map = hazelcastInstance.getMap("my-distributed-map");
        map.put(key, value);
    }

	@Override
    public String get(String key) {
        IMap<String, String> map = hazelcastInstance.getMap("my-distributed-map");
        return map.get(key);
    }

	private String doSomethingToGet() {
		String message = "";
		try {
			System.out.println("durmiendo...");
			Thread.sleep(3000);
			System.out.println("levantado.");
			
			IMap<String, String> map = hazelcastInstance.getMap("my-distributed-map");
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
			message = "termino";
		}
		return message;
    }
}