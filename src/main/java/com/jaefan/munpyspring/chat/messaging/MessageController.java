package com.jaefan.munpyspring.chat.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.jaefan.munpyspring.chat.application.ChatService;
import com.jaefan.munpyspring.chat.presentation.dto.ChatMessageDto;

@Controller
public class MessageController {

	@Autowired
	private RabbitMqClient rabbitMqClient;

	@Autowired
	private ChatService messageService;

	// 클라이언트에서 "/app/chat"로 보낸 메시지를 처리
	@MessageMapping("/chat")
	public void sendMessage(ChatMessageDto message) {
		messageService.handleMessage(message);
	}
}
