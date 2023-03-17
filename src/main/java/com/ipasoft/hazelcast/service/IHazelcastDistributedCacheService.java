package com.ipasoft.hazelcast.service;

public interface IHazelcastDistributedCacheService {

	public String getHazelcastDemo();
	public String get(String key);
	public void put(String key, String value);
}