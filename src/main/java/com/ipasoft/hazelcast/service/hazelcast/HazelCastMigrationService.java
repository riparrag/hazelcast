package com.ipasoft.hazelcast.service.hazelcast;

import org.springframework.stereotype.Service;

import com.ipasoft.hazelcast.model.entity.redis.Root;
import com.ipasoft.hazelcast.repository.redis.IRoot3LevelsRedisRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HazelCastMigrationService implements IHazelCastMigrationService {
	
	private final IHazelcastDistributedCacheService hazelcastDistributedCacheService;
	private final IRoot3LevelsRedisRepository root3LevelsRedisRepository;
	
	public HazelCastMigrationService(IHazelcastDistributedCacheService hazelcastDistributedCacheService, IRoot3LevelsRedisRepository root3LevelsRedisRepository) {
		this.hazelcastDistributedCacheService = hazelcastDistributedCacheService;
		this.root3LevelsRedisRepository = root3LevelsRedisRepository;
	}
	
	public void moveAllRootsFromRedisToHazelcast() {
		Iterable<Root> roots = this.root3LevelsRedisRepository.findAll();
		
		int i = 1;
		for (Root root : roots) {
			log.info(i++ + ". persitiendo en redis: {}", root);
			this.hazelcastDistributedCacheService.putRoot(root);
		}
	}	
}