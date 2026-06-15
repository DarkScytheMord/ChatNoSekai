package com.chat.chat_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.chat_service.model.Conversation;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}