package com.chat.chat_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chat.chat_service.dto.MessageResponse;
import com.chat.chat_service.dto.SendMessageRequest;
import com.chat.chat_service.model.MessageEntity;
import com.chat.chat_service.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationService conversationService;

    public MessageService(
            MessageRepository messageRepository,
            ConversationService conversationService
    ) {
        this.messageRepository = messageRepository;
        this.conversationService = conversationService;
    }

    public MessageResponse sendMessage(SendMessageRequest request, Long senderId) {
        if (!conversationService.userBelongsToConversation(request.conversationId(), senderId)) {
            throw new RuntimeException("No perteneces a esta conversación");
        }

        MessageEntity message = new MessageEntity(
                request.conversationId(),
                senderId,
                request.content(),
                "TEXT",
                LocalDateTime.now()
        );

        MessageEntity savedMessage = messageRepository.save(message);

        return mapToResponse(savedMessage);
    }

    public List<MessageResponse> getHistory(Long conversationId, Long userId) {
        if (!conversationService.userBelongsToConversation(conversationId, userId)) {
            throw new RuntimeException("No perteneces a esta conversación");
        }

        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private MessageResponse mapToResponse(MessageEntity message) {
        return new MessageResponse(
                message.getId(),
                message.getConversationId(),
                message.getSenderId(),
                message.getContent(),
                message.getType(),
                message.getSentAt()
        );
    }
}
