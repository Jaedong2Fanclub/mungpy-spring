package com.jaefan.munpyspring.chat.event.handler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.jaefan.munpyspring.chat.domain.ChatMessage;
import com.jaefan.munpyspring.chat.event.ChatMessageSavedEvent;
import com.jaefan.munpyspring.chat.messaging.RabbitMqClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatMessageEventHandler {

	private final RabbitMqClient rabbitMqClient;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handle(ChatMessageSavedEvent event) {
		ChatMessage message = event.getChatMessage();
		rabbitMqClient.sendMessage(message);
	}
}
