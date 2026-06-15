package com.chat.auth_service.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @Email(message = "El Correo Tiene que Tener un Formato Valido")
    @NotBlank(message = "El Correo es Obligatorio")
    String email,

    @NotBlank(message = "La Contraseña es Obligatoria")
    String password
) {

}
