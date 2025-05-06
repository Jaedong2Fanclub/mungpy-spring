package com.jaefan.munpyspring.chat.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jaefan.munpyspring.chat.domain.ChatMessage;

@Component
public class RabbitMqClient {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void sendMessage(ChatMessage message) {
		rabbitTemplate.convertAndSend("Mungpy", "", message);
	}
}
