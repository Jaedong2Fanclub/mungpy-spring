package com.jaefan.munpyspring.chat.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaefan.munpyspring.animal.domain.model.ProtectionAnimal;
import com.jaefan.munpyspring.animal.domain.model.ProtectionAnimalImage;
import com.jaefan.munpyspring.animal.domain.repository.ProtectionAnimalRepository;
import com.jaefan.munpyspring.chat.domain.ChatMessage;
import com.jaefan.munpyspring.chat.domain.ChatRoom;
import com.jaefan.munpyspring.chat.domain.repository.ChatMessageRepository;
import com.jaefan.munpyspring.chat.domain.repository.ChatRoomRepository;
import com.jaefan.munpyspring.chat.event.ChatMessageSavedEvent;
import com.jaefan.munpyspring.chat.presentation.dto.ChatEnterDto;
import com.jaefan.munpyspring.chat.presentation.dto.ChatMessageDto;
import com.jaefan.munpyspring.chat.presentation.dto.ChatRoomDto;
import com.jaefan.munpyspring.shelter.domain.model.Shelter;
import com.jaefan.munpyspring.shelter.domain.repository.ShelterRepository;
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
	private final ShelterRepository shelterRepository;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Transactional
	public void handleMessage(ChatMessageDto message) {
		ChatMessage chatMessage = chatMessageRepository.save(
			ChatMessage.create(message.getRoomId(), message.getSenderId(), message.getContent())
		);
		applicationEventPublisher.publishEvent(new ChatMessageSavedEvent(chatMessage));
	}

	public List<ChatRoomDto> findRooms(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("User not found has userId:" + userId));
		Optional<Shelter> optionalShelter = shelterRepository.findByUserId(userId);
		List<ChatRoom> chatRooms = new ArrayList<>();
		boolean isShelter = optionalShelter.isPresent();
		if (isShelter) {
			Shelter shelter = optionalShelter.get();
			chatRooms = chatRoomRepository.findByShelterId(shelter.getId());
		} else {
			chatRooms = chatRoomRepository.findByUserId(user.getId());
		}
		return getChatRoomDto(chatRooms, isShelter);
	}

	@Transactional
	public ChatEnterDto createOrGetChatRoom(Long userId, Long animalId) {
		Optional<ChatRoom> chatRoom = chatRoomRepository.findByUserIdAndProtectionAnimalId(userId, animalId);
		if (chatRoom.isPresent()) {
			Long roomId = chatRoom.get().getId();
			return new ChatEnterDto(userId, roomId, readInfiniteScroll(roomId, null));
		} else {
			ProtectionAnimal protectionAnimal = protectionAnimalRepository.findById(animalId)
				.orElseThrow(() -> new EntityNotFoundException());
			User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException());
			ChatRoom newChatRoom = ChatRoom.create(protectionAnimal, user, protectionAnimal.getShelter());
			chatRoomRepository.save(newChatRoom);
			return new ChatEnterDto(userId, newChatRoom.getId(), Collections.emptyList());
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

	public List<ChatRoomDto> getChatRoomDto(List<ChatRoom> chatRooms, boolean isShelter) {
		List<ChatRoomDto> list = new ArrayList<>();
		for (ChatRoom chatRoom : chatRooms) {
			Long roomId = chatRoom.getId();
			ProtectionAnimal protectionAnimal = chatRoom.getProtectionAnimal();
			ProtectionAnimalImage protectionAnimalImage = protectionAnimal.getProtectionAnimalImages()
				.stream()
				.findFirst()
				.orElse(null);
			String imageUrl = protectionAnimalImage == null ? null : protectionAnimalImage.getImageUrl();
			String partnerName = isShelter ? chatRoom.getUser().getNickname() : chatRoom.getShelter().getName();
			ChatMessage chatMessage = chatMessageRepository.readLastMessage(roomId, 1L);
			String lastMessage = chatMessage == null ? null : chatMessage.getContent();
			LocalDateTime lastMessageDate = chatMessage == null ? null : chatMessage.getCreatedAt();
			list.add(
				new ChatRoomDto(roomId, imageUrl, partnerName, lastMessage, lastMessageDate)
			);
		}
		return list;
	}
}
