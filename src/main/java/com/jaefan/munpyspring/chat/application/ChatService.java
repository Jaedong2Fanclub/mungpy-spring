package com.jaefan.munpyspring.chat.application;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaefan.munpyspring.animal.domain.model.ProtectionAnimal;
import com.jaefan.munpyspring.animal.domain.repository.ProtectionAnimalRepository;
import com.jaefan.munpyspring.chat.domain.ChatMessage;
import com.jaefan.munpyspring.chat.domain.ChatRoom;
import com.jaefan.munpyspring.chat.domain.repository.ChatMessageRepository;
import com.jaefan.munpyspring.chat.domain.repository.ChatRoomRepository;
import com.jaefan.munpyspring.chat.event.ChatMessageSavedEvent;
import com.jaefan.munpyspring.chat.presentation.dto.ChatEnterDto;
import com.jaefan.munpyspring.chat.presentation.dto.ChatMessageDto;
import com.jaefan.munpyspring.user.domain.model.User;
import com.jaefan.munpyspring.user.domain.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final ChatMessageRepository chatMessageRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ProtectionAnimalRepository protectionAnimalRepository;
	private final UserRepository userRepository;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Transactional
	public void handleMessage(ChatMessageDto message) {
		ChatMessage chatMessage = chatMessageRepository.save(
			ChatMessage.create(message.getRoomId(), message.getSenderId(), message.getContent())
		);
		applicationEventPublisher.publishEvent(new ChatMessageSavedEvent(chatMessage));
	}

	@Transactional
	public ChatEnterDto createOrGetChatRoom(Long userId, Long animalId) {
		Optional<ChatRoom> chatRoom = chatRoomRepository.findByUserIdAndProtectionAnimalId(userId, animalId);
		if (chatRoom.isPresent()) {
			Long roomId = chatRoom.get().getId();
			return ChatEnterDto.init(userId, roomId, readInfiniteScroll(roomId, null));
		} else {
			ProtectionAnimal protectionAnimal = protectionAnimalRepository.findById(animalId)
				.orElseThrow(() -> new EntityNotFoundException());
			User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException());
			ChatRoom newChatRoom = ChatRoom.create(protectionAnimal, user, protectionAnimal.getShelter());
			chatRoomRepository.save(newChatRoom);
			return ChatEnterDto.init(userId, newChatRoom.getId(), Collections.emptyList());
		}
	}

	public List<ChatMessageDto> readInfiniteScroll(Long roomId, Long lastMessageId) {
		if (!chatRoomRepository.existsById(roomId)) {
			throw new EntityNotFoundException();
		}
		List<ChatMessage> messages = lastMessageId == null ?
			chatMessageRepository.readInfiniteScroll(roomId, 20L) :
			chatMessageRepository.readInfiniteScroll(roomId, lastMessageId, 20L);
		return messages.stream().map(ChatMessageDto::from).toList();
	}
}
