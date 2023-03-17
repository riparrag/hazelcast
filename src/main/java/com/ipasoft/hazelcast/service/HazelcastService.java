package com.ipasoft.hazelcast.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

//import com.ipasoft.hazelcast.controller.HazelcastInstance;
//import com.ipasoft.hazelcast.controller.IMap;

@Service
public class HazelcastService implements IHazelcastService {

	//@Autowired private HazelcastInstance hazelcastInstance;
	
	@Cacheable("ipa-hazel")
	public String getHazelcastDemo() {
		return doSomethingToGet();
	}
	
	public String doSomethingToGet() {
		String message = "";
		try {
			System.out.println("durmiendo...");
			Thread.sleep(3000);
			System.out.println("levantado.");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			message = "termino";
		}
		return message;
		/*
        IMap<String, String> map = hazelcastInstance.getMap("my-distributed-map");
        map.put("1", "Hello");
        map.put("2", "World");
        return "Map size: " + map.size() + "\n" +
                "Value for key '1': " + map.get("1") + "\n" +
                "Value for key '2': " + map.get("2");
        */
    }
}
