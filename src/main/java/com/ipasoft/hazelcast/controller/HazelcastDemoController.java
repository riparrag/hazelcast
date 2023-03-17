package com.ipasoft.hazelcast.controller;

import org.springframework.web.bind.annotation.GetMapping;
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
}