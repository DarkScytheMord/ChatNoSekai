package com.chat.bot_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JikanSearchResponse(
        List<JikanMediaItem> data
) {
}
