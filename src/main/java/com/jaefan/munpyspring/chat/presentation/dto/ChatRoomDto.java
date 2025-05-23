package com.jaefan.munpyspring.chat.presentation.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomDto {
	private Long roomId;
	private String imageUrl;
	private String partnerName;
	private String lastMessage;
	private LocalDateTime lastMessageDate;
}
