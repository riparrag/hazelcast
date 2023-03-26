package com.ipasoft.hazelcast.repository.redis;

import org.springframework.data.repository.CrudRepository;

import com.ipasoft.hazelcast.model.entity.redis.StudentRedis;

public interface IStudentRedisRepository extends CrudRepository<StudentRedis, String> {
	
}