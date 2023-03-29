package com.ipasoft.hazelcast.model.entity.redis;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Level3 implements Serializable {
	private static final long serialVersionUID = -3600929969601740550L;

	private String prop5;
	private String prop6;
	private int prop7;
	private double prop8;
	private Date prop9;
}