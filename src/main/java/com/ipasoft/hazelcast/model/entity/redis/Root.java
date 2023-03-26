package com.ipasoft.hazelcast.model.entity.redis;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;
//import com.hazelcast.query.annotations.HazelcastIndexed;

import lombok.Data;

@Data
@RedisHash("Root")
//@HazelcastIndexed
public class Root implements Serializable {
	private static final long serialVersionUID = 113172895436855785L;

	private int id;
    private Level1 level1;
}