package com.ipasoft.hazelcast.model.entity.redis;

import java.io.Serializable;

import lombok.Data;

@Data
public class Level2 implements Serializable {
	private static final long serialVersionUID = 3565229937248324369L;

	private String prop3;
    private String prop4;
    private Level3 level3;
}