package com.jaefan.munpyspring.chat.presentation;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jaefan.munpyspring.chat.application.ChatService;
import com.jaefan.munpyspring.chat.presentation.dto.ChatEnterDto;
import com.jaefan.munpyspring.chat.presentation.dto.ChatMessageDto;
import com.jaefan.munpyspring.chat.presentation.dto.ChatRoomDto;
import com.jaefan.munpyspring.user.domain.model.User;
import com.jaefan.munpyspring.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
	private final ChatService chatService;
	private final UserRepository userRepository;

	@GetMapping("/rooms/{roomId}")
	public ChatEnterDto initChatRoom(@PathVariable Long roomId) {
		Long userId = getCurrentUserId();
		List<ChatMessageDto> messages = chatService.readInfiniteScroll(roomId, null);

		return new ChatEnterDto(userId, roomId, messages);
	}

	@GetMapping("/rooms")
	public List<ChatRoomDto> getRooms() {
		return chatService.findRooms(getCurrentUserId());
	}

	@GetMapping("/animals/{animalId}")
	public ChatEnterDto getChatRoom(@PathVariable Long animalId) {
		Long userId = getCurrentUserId();
		return chatService.createOrGetChatRoom(userId, animalId);
	}

	@GetMapping("/rooms/{roomId}/messages")
	public List<ChatMessageDto> loadPreviousMessage(
		@PathVariable Long roomId,
		@RequestParam(required = false) Long lastMessageId
	) {
		return chatService.readInfiniteScroll(roomId, lastMessageId);
	}

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User user = userRepository.findByEmail(email).orElseThrow();
		return user.getId();
	}
}
