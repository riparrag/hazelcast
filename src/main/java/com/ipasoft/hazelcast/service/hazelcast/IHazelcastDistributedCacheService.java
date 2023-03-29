package com.ipasoft.hazelcast.service.hazelcast;

import java.util.Map;

import com.ipasoft.hazelcast.model.entity.redis.Root;

public interface IHazelcastDistributedCacheService {
	
	public final static String DISTRIBUTED_STRING_MAP = "string_map";
	public final static String DISTRIBUTED_ROOT_MAPS  = "root_maps";

	public String getHazelcastDemoMethodCache();
	
	public String getCachedString(String key);
	public void putCachedString(String key, String value);
	
	public Root getCachedData(int key);
	//public CachedData<?> putCachedData(CachedData<?> data);
	public Root putCachedData(Root data);

//	public Map<Object, Object> getAllCachedData();
	public Map<Integer, Root> getAllCachedData(Integer... keys);
	
	public Root putRoot(Root root);
	
	public Iterable<Root> getFilteredRoots();
}