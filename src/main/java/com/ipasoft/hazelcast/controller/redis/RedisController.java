package com.ipasoft.hazelcast.controller.redis;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ipasoft.hazelcast.model.entity.redis.Root;
import com.ipasoft.hazelcast.model.entity.redis.StudentRedis;
import com.ipasoft.hazelcast.service.redis.IRedisService;

@RestController
@RequestMapping("/redis")
public class RedisController {

	private final IRedisService redisService;
	
	public RedisController(IRedisService redisService) {
		this.redisService = redisService;
	}
	
	@GetMapping("/get-value-from-redis/{key}")
	public String getValueFromRedis(@PathVariable String key) {
		return this.redisService.getValueFromRedis(key);
	}
	
	@GetMapping("/get-all-students")
	public Iterable<StudentRedis> getAll() {
		return this.redisService.getAllStudents();
	}
	
	@GetMapping("/get-all-roots")
	public Iterable<Root> getAllRoots() {
		return this.redisService.getAllRoots();
	}
	
	@GetMapping("/save-all-roots-from-json")
	public void processAll() {
		try {
			this.redisService.saveAllRootsFromJson();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@PostMapping()
	@ResponseStatus(code = HttpStatus.CREATED)
	public StudentRedis createStudent(@RequestBody StudentRedis student) {
		return this.redisService.saveStudent( student );
	}
}