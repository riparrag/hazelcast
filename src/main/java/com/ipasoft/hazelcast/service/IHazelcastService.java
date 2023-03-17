package com.ipasoft.hazelcast.service;

public interface IHazelcastService {

	public String getHazelcastDemo();
	public String get(String key);
	public void put(String key, String value);
}