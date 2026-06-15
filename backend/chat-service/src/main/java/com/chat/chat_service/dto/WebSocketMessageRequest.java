package com.chat.chat_service.dto;

public record WebSocketMessageRequest(
        Long conversationId,
        String content
) {
}
