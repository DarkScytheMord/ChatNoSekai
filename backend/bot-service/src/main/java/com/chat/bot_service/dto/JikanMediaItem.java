package com.chat.bot_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JikanMediaItem(
        @JsonProperty("mal_id")
        Long malId,

        String url,

        String title,

        @JsonProperty("title_english")
        String titleEnglish,

        String synopsis,

        Double score,

        Integer episodes,

        Integer chapters,

        Integer volumes,

        String type,

        String status,

        JikanImages images
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record JikanImages(
            JikanImage jpg,
            JikanImage webp
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record JikanImage(
            @JsonProperty("image_url")
            String imageUrl,

            @JsonProperty("large_image_url")
            String largeImageUrl
    ) {
    }
}