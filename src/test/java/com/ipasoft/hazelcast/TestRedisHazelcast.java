package com.ipasoft.hazelcast;

import java.io.File;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.ipasoft.hazelcast.model.entity.redis.Root;

import redis.clients.jedis.Jedis;

public class TestRedisHazelcast {
	public static void main(String[] args) {
		testPutInfoInHazelcast();

		testGetInfoHazelcast();

		testPut20ThousandsJson();
	}

	private static void testPutInfoInHazelcast() {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.setClusterName("hello-world");

		HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
		IMap<String, String> map = client.getMap("my-distributed-map");

		map.put("1", "ro");
		map.put("2", "ivaldis");
		map.put("3", "grosos");

		client.shutdown();
	}

	private static void testGetInfoHazelcast() {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.setClusterName("hello-world");

		HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

		IMap<String, String> map = client.getMap("my-distributed-map");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}

		client.shutdown();
	}
	
	private static void testPut20ThousandsJson() {        
		Jedis jedis = null;
		try {
			// Crear una instancia de Jedis para conectarte a Redis
			jedis = new Jedis("127.0.0.1", 6379);
			
			// Leer el archivo JSON
			File file = new File("C:\\Users\\rodri\\OneDrive\\Documentos\\dev\\others\\json-poc.json");
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object>[] jsonArray;
			jsonArray = objectMapper.readValue(file, Map[].class);
        
	        // Iterar sobre los objetos JSON y guardarlos en Redis
	        for (int i = 0; i < jsonArray.length; i++) {
	            Root root = objectMapper.convertValue(jsonArray[i], Root.class);
	        	
	            String key = "json:" + i;
	            String value = objectMapper.writeValueAsString(jsonArray[i]);
	            jedis.set(key, value);
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (jedis != null) {				
				jedis.close(); //Cerrar la conexión a Redis
			}
		}
	}
}