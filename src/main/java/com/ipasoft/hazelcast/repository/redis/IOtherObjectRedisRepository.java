package com.ipasoft.hazelcast.repository.redis;

import org.springframework.data.repository.CrudRepository;

import com.ipasoft.hazelcast.model.entity.redis.OtherObject;

public interface IOtherObjectRedisRepository extends CrudRepository<OtherObject,Integer> {
	
}