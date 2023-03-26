package com.ipasoft.hazelcast.model.entity.hazelcast;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CachedData<T extends Serializable> implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private T value;
}