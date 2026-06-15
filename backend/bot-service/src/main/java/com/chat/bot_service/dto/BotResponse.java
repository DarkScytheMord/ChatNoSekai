package com.chat.bot_service.dto;

import java.time.LocalDateTime;

public record BotResponse(
        Long id,
        Long userId,
        String question,
        String answer,
        LocalDateTime createdAt
) {
}