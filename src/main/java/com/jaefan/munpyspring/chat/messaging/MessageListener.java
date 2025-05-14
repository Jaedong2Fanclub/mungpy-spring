package com.jaefan.munpyspring.chat.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.jaefan.munpyspring.chat.domain.ChatMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageListener {
	private final SimpMessagingTemplate messagingTemplate;

	private final RabbitConstant rabbitConstant;

	@RabbitListener(queues = "#{rabbitConstant.getQueueName()}")
	public void receiveMessage(ChatMessage message) {
		String roomId = String.valueOf(message.getRoomId());
		messagingTemplate.convertAndSend("/topic/chat/" + roomId, message);
	}
}
