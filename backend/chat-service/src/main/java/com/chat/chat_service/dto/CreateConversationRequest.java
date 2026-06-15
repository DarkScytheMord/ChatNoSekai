package com.chat.chat_service.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record CreateConversationRequest(
        String title,

        @NotEmpty(message = "Debe agregar al menos un usuario a la conversación")
        List<Long> userIds
) {
}
