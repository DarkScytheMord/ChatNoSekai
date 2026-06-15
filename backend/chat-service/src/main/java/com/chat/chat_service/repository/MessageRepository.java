package com.chat.chat_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.chat_service.model.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findByConversationIdOrderBySentAtAsc(Long conversationId);
}
