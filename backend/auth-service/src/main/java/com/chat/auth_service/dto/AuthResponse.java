package com.chat.auth_service.dto;

public record AuthResponse(
    Long userId,
    String fullName,
    String email,
    String role,
    String token
) {

}
