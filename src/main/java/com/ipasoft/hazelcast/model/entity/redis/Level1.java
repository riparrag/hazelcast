package com.ipasoft.hazelcast.model.entity.redis;

import java.io.Serializable;

import lombok.Data;

@Data
public class Level1 implements Serializable {
	private static final long serialVersionUID = 6882865025487867760L;

	private String prop1;
	private String prop2;
	private Level2 level2;
}