package com.ipasoft.hazelcast.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipasoft.hazelcast.service.IHazelcastService;

@RestController
@RequestMapping("/hazelcast")
public class HazelcastDemoController {

    private final IHazelcastService hazelcastService;

    public HazelcastDemoController(IHazelcastService hazelcastService) {
		this.hazelcastService = hazelcastService;
	}
    
    @GetMapping("/hazelcast-demo")
    public String getHazelcastDemo() {
        return this.hazelcastService.getHazelcastDemo();
    }
    
    @GetMapping("/get-value/{key}")
    public String getValue(@PathVariable String key) {
        return this.hazelcastService.get(key);
    }
    
    @PutMapping("/put-key-value/{key}/{value}")
    public void putKey(@PathVariable String key, @PathVariable String value) {
        this.hazelcastService.put(key,value);
    }
}