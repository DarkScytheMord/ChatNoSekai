package com.chat.chat_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.chat_service.model.ConversationMember;

public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Long> {

    List<ConversationMember> findByUserId(Long userId);

    List<ConversationMember> findByConversationId(Long conversationId);

    boolean existsByConversationIdAndUserId(Long conversationId, Long userId);
}