package com.ipasoft.hazelcast.service;

public interface IHazelcastDistributedCacheService {
	
	public final static String MY_DISTRIBUTED_MAP = "my-distributed-map";

	public String getHazelcastDemo();
	public String get(String key);
	public void put(String key, String value);
}