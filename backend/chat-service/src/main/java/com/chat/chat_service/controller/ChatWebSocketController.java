package com.chat.chat_service.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;

import com.chat.chat_service.dto.MessageResponse;
import com.chat.chat_service.dto.SendMessageRequest;
import com.chat.chat_service.dto.WebSocketMessageRequest;
import com.chat.chat_service.service.MessageService;

import java.security.Principal;

@Controller
public class ChatWebSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocketController(
            MessageService messageService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(WebSocketMessageRequest request, Principal principal) {
        Long senderId = getUserIdFromPrincipal(principal);

        SendMessageRequest sendMessageRequest = new SendMessageRequest(
                request.conversationId(),
                request.content()
        );

        MessageResponse savedMessage = messageService.sendMessage(sendMessageRequest, senderId);

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + request.conversationId(),
                savedMessage
        );
    }

    private Long getUserIdFromPrincipal(Principal principal) {
        if (!(principal instanceof JwtAuthenticationToken jwtAuthenticationToken)) {
            throw new RuntimeException("Usuario no autenticado en WebSocket");
        }

        Jwt jwt = jwtAuthenticationToken.getToken();
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