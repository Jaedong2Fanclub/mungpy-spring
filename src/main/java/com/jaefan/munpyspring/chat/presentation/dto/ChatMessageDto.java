package com.jaefan.munpyspring.chat.presentation.dto;

import java.time.LocalDateTime;

import com.jaefan.munpyspring.chat.domain.ChatMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ChatMessageDto {
	private Long chatMessageId;
	private Long roomId;
	private Long senderId;
	private String content;
	private LocalDateTime createdAt;

	public static ChatMessageDto from(ChatMessage message) {
		return ChatMessageDto.builder()
			.chatMessageId(message.getChatMessageId())
			.roomId(message.getRoomId())
			.senderId(message.getSenderId())
			.content(message.getContent())
			.createdAt(message.getCreatedAt())
			.build();
	}
}
