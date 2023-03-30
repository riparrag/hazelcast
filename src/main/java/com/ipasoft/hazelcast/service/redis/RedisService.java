package com.ipasoft.hazelcast.service.redis;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasoft.hazelcast.model.entity.redis.OtherObject;
import com.ipasoft.hazelcast.model.entity.redis.Root;
import com.ipasoft.hazelcast.model.entity.redis.StudentRedis;
import com.ipasoft.hazelcast.repository.redis.IOtherObjectRedisRepository;
import com.ipasoft.hazelcast.repository.redis.IRoot3LevelsRedisRepository;
import com.ipasoft.hazelcast.repository.redis.IStudentRedisRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RedisService implements IRedisService {
	private final RedisTemplate<String, String> redisTemplate;
	private final IStudentRedisRepository studentRedisRepository;
	private final IRoot3LevelsRedisRepository root3LevelsRedisRepository;
	private final IOtherObjectRedisRepository otherObjectRedisRepository;

	public RedisService(RedisTemplate<String, String> redisTemplate, IStudentRedisRepository studentRedisRepository, IRoot3LevelsRedisRepository root3LevelsRedisRepository, IOtherObjectRedisRepository otherObjectRedisRepository) {
		this.redisTemplate = redisTemplate; 
		this.studentRedisRepository = studentRedisRepository;
		this.root3LevelsRedisRepository = root3LevelsRedisRepository;
		this.otherObjectRedisRepository = otherObjectRedisRepository;
	}
	
    public String getValueFromRedis(String key) {
        return this.redisTemplate.opsForValue().get( key );
    }
	
    public StudentRedis saveStudent(StudentRedis student) {
    	return this.studentRedisRepository.save( student );
	}
    
    public Iterable<StudentRedis> getAllStudents() {
		return this.studentRedisRepository.findAll();
	}
    
    public Iterable<Root> getAllRoots() {
		return this.root3LevelsRedisRepository.findAll();
	}

	@Override
	public void saveAllRootsFromJson() throws StreamReadException, DatabindException, IOException {

		File file = new File("C:\\Users\\rodri\\OneDrive\\Documentos\\dev\\others\\json-poc.json");
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object>[] jsonArray;
		jsonArray = objectMapper.readValue(file, Map[].class);
    
        // Iterar sobre los objetos JSON y guardarlos en Redis
        for (int i=0; i < jsonArray.length; i++) {
        	OtherObject otherObject = objectMapper.convertValue(jsonArray[i], OtherObject.class);
        	log.info(i + ". persitiendo otherObject en redis: {}", otherObject);
        	this.otherObjectRedisRepository.save( otherObject );
        	
            //Root root = objectMapper.convertValue(jsonArray[i], Root.class);
            //log.info(i + ". persitiendo root en redis: {}", root);
            //this.root3LevelsRedisRepository.save( root );
        }
	}
}