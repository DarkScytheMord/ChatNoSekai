package com.chat.bot_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BotQuestionRequest(
        @NotBlank(message = "La pregunta no puede estar vacía")
        @Size(max = 2000, message = "La pregunta no puede superar los 2000 caracteres")
        String question
) {
}