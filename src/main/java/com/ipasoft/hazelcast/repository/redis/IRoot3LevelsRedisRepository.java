package com.ipasoft.hazelcast.repository.redis;

import org.springframework.data.repository.CrudRepository;

import com.ipasoft.hazelcast.model.entity.redis.Root;

public interface IRoot3LevelsRedisRepository extends CrudRepository<Root,Integer> {
	
}