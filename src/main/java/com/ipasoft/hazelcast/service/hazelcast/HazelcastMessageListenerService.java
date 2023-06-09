package com.ipasoft.hazelcast.service.hazelcast;

import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;

@Service
public class HazelcastMessageListenerService implements IHazelcastMessageListenerService {

	private final HazelcastInstance hazelcastInstance;
	
	public HazelcastMessageListenerService(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
	}
	
	@Override
    public void publish(String message) {
		ITopic<String> topic = this.hazelcastInstance.getTopic("ivaldis-topic");
		 
		topic.addMessageListener(new MessageListener<String>() {
			public void onMessage(Message<String> message) {
				String value = message.getMessageObject();
				System.out.println("Received message from ivaldis-topic: " + value);
			}
		});
		topic.publish("Hello, world ivaldis-topic!");
    }
}