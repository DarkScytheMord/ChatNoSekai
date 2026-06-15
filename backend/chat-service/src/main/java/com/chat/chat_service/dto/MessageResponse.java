package com.chat.chat_service.dto;

import java.time.LocalDateTime;

public record MessageResponse(
        Long id,
        Long conversationId,
        Long senderId,
        String content,
        String type,
        LocalDateTime sentAt
) {
}
