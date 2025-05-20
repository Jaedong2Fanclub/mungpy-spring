package com.jaefan.munpyspring.chat.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jaefan.munpyspring.chat.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
	// 사용자 ID와 유기견 ID로 채팅방을 찾는 메소드 (나의 채팅 목록이 아닌 동물 상세 페이지에서 채팅하기 클릭 시 발생)
	Optional<ChatRoom> findByUserIdAndProtectionAnimalId(Long userId, Long animalId);

	List<ChatRoom> findByShelterId(Long shelterId);

	List<ChatRoom> findByUserId(Long userId);
}
