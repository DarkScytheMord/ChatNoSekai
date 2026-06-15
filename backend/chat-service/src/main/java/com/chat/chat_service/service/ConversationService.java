package com.chat.chat_service.service;

import org.springframework.stereotype.Service;

import com.chat.chat_service.dto.ConversationResponse;
import com.chat.chat_service.dto.CreateConversationRequest;
import com.chat.chat_service.model.Conversation;
import com.chat.chat_service.model.ConversationMember;
import com.chat.chat_service.repository.ConversationMemberRepository;
import com.chat.chat_service.repository.ConversationRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository memberRepository;

    public ConversationService(
            ConversationRepository conversationRepository,
            ConversationMemberRepository memberRepository
    ) {
        this.conversationRepository = conversationRepository;
        this.memberRepository = memberRepository;
    }

    public ConversationResponse createConversation(CreateConversationRequest request, Long creatorUserId) {
        Set<Long> memberIds = new LinkedHashSet<>(request.userIds());
        memberIds.add(creatorUserId);

        Conversation conversation = new Conversation(
                request.title(),
                memberIds.size() > 2 ? "GROUP" : "DIRECT",
                LocalDateTime.now()
        );

        Conversation savedConversation = conversationRepository.save(conversation);

        for (Long userId : memberIds) {
            ConversationMember member = new ConversationMember(
                    savedConversation.getId(),
                    userId,
                    LocalDateTime.now()
            );

            memberRepository.save(member);
        }

        return new ConversationResponse(
                savedConversation.getId(),
                savedConversation.getTitle(),
                savedConversation.getType(),
                savedConversation.getCreatedAt(),
                new ArrayList<>(memberIds)
        );
    }

    public List<ConversationResponse> getMyConversations(Long userId) {
        List<ConversationMember> memberships = memberRepository.findByUserId(userId);

        return memberships.stream()
                .map(member -> conversationRepository.findById(member.getConversationId())
                        .orElse(null))
                .filter(Objects::nonNull)
                .map(conversation -> {
                    List<Long> members = memberRepository.findByConversationId(conversation.getId())
                            .stream()
                            .map(ConversationMember::getUserId)
                            .collect(Collectors.toList());

                    return new ConversationResponse(
                            conversation.getId(),
                            conversation.getTitle(),
                            conversation.getType(),
                            conversation.getCreatedAt(),
                            members
                    );
                })
                .collect(Collectors.toList());
    }

    public boolean userBelongsToConversation(Long conversationId, Long userId) {
        return memberRepository.existsByConversationIdAndUserId(conversationId, userId);
    }
}