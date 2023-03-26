package com.ipasoft.hazelcast.service.hazelcast;

import java.util.Map;

import com.ipasoft.hazelcast.model.entity.hazelcast.CachedData;
import com.ipasoft.hazelcast.model.entity.redis.Root;

public interface IHazelcastDistributedCacheService {
	
	public final static String MY_DISTRIBUTED_MAP = "my-distributed-map";

	public String getHazelcastDemoMethodCache();
	
	public String getCachedString(String key);
	public void putCachedString(String key, String value);
	
	public CachedData<?> getCachedData(int key);
	public CachedData<?> putCachedData(CachedData<?> data);

	public Map<Object, Object> getAllCachedData();

	public Root putRoot(Root root);
}