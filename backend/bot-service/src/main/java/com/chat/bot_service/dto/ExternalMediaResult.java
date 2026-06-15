package com.chat.bot_service.dto;

public record ExternalMediaResult(
        Long malId,
        String mediaCategory,
        String title,
        String titleEnglish,
        String synopsis,
        Double score,
        Integer episodes,
        Integer chapters,
        Integer volumes,
        String type,
        String status,
        String url,
        String imageUrl
) {
}
