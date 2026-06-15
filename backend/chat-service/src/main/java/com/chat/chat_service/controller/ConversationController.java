package com.chat.chat_service.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.chat.chat_service.dto.ConversationResponse;
import com.chat.chat_service.dto.CreateConversationRequest;
import com.chat.chat_service.service.ConversationService;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    public ResponseEntity<ConversationResponse> createConversation(
            @Valid @RequestBody CreateConversationRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = getUserIdFromJwt(jwt);
        return ResponseEntity.ok(conversationService.createConversation(request, userId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ConversationResponse>> getMyConversations(
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = getUserIdFromJwt(jwt);
        return ResponseEntity.ok(conversationService.getMyConversations(userId));
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