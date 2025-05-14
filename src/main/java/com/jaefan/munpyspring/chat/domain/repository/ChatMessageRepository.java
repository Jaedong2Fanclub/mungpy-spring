package com.jaefan.munpyspring.chat.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jaefan.munpyspring.chat.domain.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
	@Query(
		value = "select * "
			+ "from chat_message "
			+ "where room_id = :roomId "
			+ "order by chat_message_id desc "
			+ "limit :limit",
		nativeQuery = true
	)
	List<ChatMessage> readInfiniteScroll(Long roomId, Long limit);

	@Query(
		value = "select * "
			+ "from chat_message "
			+ "where room_id = :roomId And chat_message_id < :lastMessageId "
			+ "order by chat_message_id desc "
			+ "limit :limit",
		nativeQuery = true
	)
	List<ChatMessage> readInfiniteScroll(Long roomId, Long lastMessageId, Long limit);
}
