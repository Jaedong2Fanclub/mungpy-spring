package com.jaefan.munpyspring.chat.messaging;

import java.util.UUID;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class RabbitConstant {
	private String queueName;

	@PostConstruct
	public void init() {
		this.queueName = "queue-" + UUID.randomUUID().toString();
	}

	public String getQueueName() {
		return queueName;
	}
}
