package com.jaefan.munpyspring.chat.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.jaefan.munpyspring.chat.domain.ChatMessage;

@Component
public class MessageListener {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private RabbitConstant rabbitConstant;

	public MessageListener(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@RabbitListener(queues = "#{rabbitConstant.getQueueName()}")
	public void receiveMessage(ChatMessage message) {
		String roomId = String.valueOf(message.getRoomId());
		messagingTemplate.convertAndSend("/topic/chat/" + roomId, message);
	}
}
