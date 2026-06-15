package com.chat.bot_service.dto;

import java.time.LocalDateTime;

public record BotHistoryResponse(
        Long id,
        String question,
        String answer,
        LocalDateTime createdAt
) {
}