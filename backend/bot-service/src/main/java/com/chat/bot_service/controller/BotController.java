package com.chat.bot_service.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.chat.bot_service.dto.BotHistoryResponse;
import com.chat.bot_service.dto.BotQuestionRequest;
import com.chat.bot_service.dto.BotResponse;
import com.chat.bot_service.service.ChatBotService;

import java.util.List;

@RestController
@RequestMapping("/api/bot")
public class BotController {

    private final ChatBotService chatBotService;

    public BotController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @PostMapping("/ask")
    public ResponseEntity<BotResponse> askQuestion(
            @Valid @RequestBody BotQuestionRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = getUserIdFromJwt(jwt);
        return ResponseEntity.ok(chatBotService.askQuestion(request, userId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<BotHistoryResponse>> getMyHistory(
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = getUserIdFromJwt(jwt);
        return ResponseEntity.ok(chatBotService.getMyHistory(userId));
    }

    private Long getUserIdFromJwt(Jwt jwt) {
        Object userIdClaim = jwt.getClaim("userId");

        if (userIdClaim instanceof Integer integerValue) {
            return integerValue.longValue();
        }

        if (userIdClaim instanceof Long longValue) {
            return longValue;
        }

        if (userIdClaim instanceof Number numberValue) {
            return numberValue.longValue();
        }

        throw new RuntimeException("El token no contiene un userId válido");
    }
}
