package com.ipasoft.hazelcast.service.hazelcast;

import org.springframework.stereotype.Service;

import com.ipasoft.hazelcast.model.entity.redis.OtherObject;
import com.ipasoft.hazelcast.model.entity.redis.Root;
import com.ipasoft.hazelcast.repository.redis.IOtherObjectRedisRepository;
import com.ipasoft.hazelcast.repository.redis.IRoot3LevelsRedisRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HazelCastMigrationService implements IHazelCastMigrationService {
	
	private final IHazelcastDistributedCacheService hazelcastDistributedCacheService;
	private final IRoot3LevelsRedisRepository root3LevelsRedisRepository;
	private final IOtherObjectRedisRepository otherObjectRedisRepository;
	
	public HazelCastMigrationService(IHazelcastDistributedCacheService hazelcastDistributedCacheService, IRoot3LevelsRedisRepository root3LevelsRedisRepository, IOtherObjectRedisRepository otherObjectRedisRepository) {
		this.hazelcastDistributedCacheService = hazelcastDistributedCacheService;
		this.root3LevelsRedisRepository = root3LevelsRedisRepository;
		this.otherObjectRedisRepository = otherObjectRedisRepository;
	}
	
	public void moveAllRootsFromRedisToHazelcast() {
		Iterable<OtherObject> otherObjects = this.otherObjectRedisRepository.findAll();
		int i = 1;
		for (OtherObject otherObject : otherObjects) {
			log.info(i++ + ". persitiendo otherObject en hazelcast: {}", otherObject);
			this.hazelcastDistributedCacheService.putOtherObject( otherObject );
		}

		Iterable<Root> roots = this.root3LevelsRedisRepository.findAll();
		i = 1;
		for (Root root : roots) {
			log.info(i++ + ". persitiendo roots en hazelcast: {}", root);
			this.hazelcastDistributedCacheService.putRoot(root);
		}
	}	
}