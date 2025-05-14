package com.jaefan.munpyspring.chat.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_message", indexes = {
	@Index(name = "idx_room_message", columnList = "room_id, chat_message_id")
})
public class ChatMessage {
	@Id
	@Column(name = "chat_message_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chatMessageId;

	@Column(name = "room_id")
	private Long roomId;

	@Column(name = "sender_id")
	private Long senderId;

	private String content;

	private LocalDateTime createdAt;

	public static ChatMessage create(Long roomId, Long senderId, String content) {
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.roomId = roomId;
		chatMessage.senderId = senderId;
		chatMessage.content = content;
		chatMessage.createdAt = LocalDateTime.now();
		return chatMessage;
	}
}
