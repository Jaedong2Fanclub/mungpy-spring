package com.jaefan.munpyspring.chat.presentation.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ChatEnterDto {
	private Long userId;
	private Long roomId;
	private List<ChatMessageDto> messages;
}
