package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RabbitMQQueue {

	private long messages;
	
	private String name;

	public boolean isDirty() {
		return messages > 0;
	}
	
}
