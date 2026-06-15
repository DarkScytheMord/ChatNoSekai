package com.chat.chat_service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ConversationResponse(
        Long id,
        String title,
        String type,
        LocalDateTime createdAt,
        List<Long> memberIds
) {
}
