package com.chat.bot_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.bot_service.model.BotMessage;

import java.util.List;

public interface BotMessageRepository extends JpaRepository<BotMessage, Long> {

    List<BotMessage> findByUserIdOrderByCreatedAtDesc(Long userId);
}