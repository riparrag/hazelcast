package com.ipasoft.hazelcast.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface IKafkaService {

    public void listen(ConsumerRecord<String, String> record);
}