package com.ipasoft.hazelcast.controller.hazelcast;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipasoft.hazelcast.model.entity.hazelcast.CachedData;
import com.ipasoft.hazelcast.model.entity.redis.Root;
import com.ipasoft.hazelcast.service.hazelcast.IHazelCastMigrationService;
import com.ipasoft.hazelcast.service.hazelcast.IHazelcastDistributedCacheService;

@RestController
@RequestMapping("/hazelcast")
public class HazelcastDemoController {

    private final IHazelcastDistributedCacheService hazelcastService;
    private final IHazelCastMigrationService hazelCastMigrationService;

    public HazelcastDemoController(IHazelcastDistributedCacheService hazelcastService, IHazelCastMigrationService hazelCastMigrationService) {
		this.hazelcastService = hazelcastService;
		this.hazelCastMigrationService = hazelCastMigrationService;
	}
    
    @GetMapping("/hazelcast-demo")
    public String getHazelcastDemo() {
        return this.hazelcastService.getHazelcastDemoMethodCache();
    }

    @GetMapping("/get-value/{key}")
    public String getValue(@PathVariable String key) {
        return this.hazelcastService.getCachedString(key);
    }
    
    @PutMapping("/put-key-value/{key}/{value}")
    public void putKey(@PathVariable String key, @PathVariable String value) {
        this.hazelcastService.putCachedString(key, value);
    }

    @PutMapping("/put-root/{key}")
    public Root putKey(@PathVariable int key, @RequestBody Root root) {
        return this.hazelcastService.putRoot( root );
    }
    
    @GetMapping("/get-root/{key}")
    private CachedData<?> getRoot(@PathVariable int key) {
    	return this.hazelcastService.getCachedData(key);
	}
    
    @GetMapping("/get-all-roots")
    public Map<Object, Object> getAllRoots() {
        return this.hazelcastService.getAllCachedData();
    }
    
    @GetMapping("/move-all-roots-from-redis-to-hazelcast")
    public void moveAllRootsFromRedisToHazelcast() {
    	this.hazelCastMigrationService.moveAllRootsFromRedisToHazelcast();
	}
}