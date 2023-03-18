package com.ipasoft.hazelcast.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaService implements IKafkaService {
    private final IHazelcastDistributedCacheService hazelcastDistributedCacheService;

    public KafkaService(IHazelcastDistributedCacheService hazelcastDistributedCacheService) {
        this.hazelcastDistributedCacheService = hazelcastDistributedCacheService;
    }

    @KafkaListener(topics ="ivaldis-topic")
    public void listen(ConsumerRecord<String, String> record) {
    	hazelcastDistributedCacheService.put(record.key(), record.value());
    }
}