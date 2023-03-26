package com.ipasoft.hazelcast.service.redis;

import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.ipasoft.hazelcast.model.entity.redis.Root;
import com.ipasoft.hazelcast.model.entity.redis.StudentRedis;

public interface IRedisService {

	public String getValueFromRedis(String key);
	public StudentRedis saveStudent(StudentRedis student);
    public Iterable<StudentRedis> getAllStudents();
    public Iterable<Root> getAllRoots();
	public void saveAllRootsFromJson() throws StreamReadException, DatabindException, IOException;
}
