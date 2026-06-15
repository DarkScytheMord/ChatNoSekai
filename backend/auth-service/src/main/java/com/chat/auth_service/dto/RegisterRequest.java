package com.chat.auth_service.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "El Nombre es Obligatorio")
    String fullName,

    @Email(message = "El Correo Tiene que Tener un Formato Valido")
    @NotBlank(message = "El Correo es Obligatorio")
    String email,

    @Size(min = 6, message = "La Contraseña Debe Tener al Menos 6 Caracteres")
    @NotBlank(message = "La Contraseña es Obligatoria")
    String password
) {
}
