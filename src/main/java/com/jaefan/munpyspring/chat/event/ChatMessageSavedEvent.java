package com.jaefan.munpyspring.chat.event;

import com.jaefan.munpyspring.chat.domain.ChatMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatMessageSavedEvent {
	private final ChatMessage chatMessage;
}
