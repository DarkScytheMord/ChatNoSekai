package com.chat.chat_service.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.chat.chat_service.dto.MessageResponse;
import com.chat.chat_service.dto.SendMessageRequest;
import com.chat.chat_service.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendMessage(
            @Valid @RequestBody SendMessageRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long senderId = getUserIdFromJwt(jwt);
        return ResponseEntity.ok(messageService.sendMessage(request, senderId));
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageResponse>> getHistory(
            @PathVariable Long conversationId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = getUserIdFromJwt(jwt);
        return ResponseEntity.ok(messageService.getHistory(conversationId, userId));
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