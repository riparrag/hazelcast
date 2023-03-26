package com.ipasoft.hazelcast.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ipasoft.hazelcast.service.hazelcast.IHazelcastDistributedCacheService;

@Service
public class KafkaService implements IKafkaService {
    private final IHazelcastDistributedCacheService hazelcastDistributedCacheService;

    public KafkaService(IHazelcastDistributedCacheService hazelcastDistributedCacheService) {
        this.hazelcastDistributedCacheService = hazelcastDistributedCacheService;
    }

    @KafkaListener(groupId="ivaldis-group", topics ="ivaldis-topic")
    public void listen(ConsumerRecord<String, String> record) {
    	hazelcastDistributedCacheService.putCachedString(record.key(), record.value());
    }
}