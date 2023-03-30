package com.ipasoft.hazelcast.model.entity.redis;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

import lombok.Data;

@Data
@RedisHash("OtherObject")
public class OtherObject implements Serializable {
	private static final long serialVersionUID = 113172895436855785L;

	private int id;
    private Level1 level1;
    private String propOther;

    public Object joinsRoot(Root root) {
		return this.getId() ==  root.getId();
	}
}