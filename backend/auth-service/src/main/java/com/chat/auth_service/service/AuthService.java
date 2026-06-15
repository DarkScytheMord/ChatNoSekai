package com.chat.auth_service.service;



import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chat.auth_service.dto.AuthResponse;
import com.chat.auth_service.dto.LoginRequest;
import com.chat.auth_service.dto.RegisterRequest;
import com.chat.auth_service.model.User;
import com.chat.auth_service.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())){
            throw new RuntimeException("El Correo ya Está Registrado");
        }

        User user = new User(
                request.fullName(),
                request.email(),
                passwordEncoder.encode(request.password()),
                "USER",
                true,
                LocalDateTime.now()
        );

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);
    
        return new AuthResponse(
            savedUser.getId(),
            savedUser.getFullName(),
            savedUser.getEmail(),
            savedUser.getRole(),
            token
        );
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new RuntimeException("Usuario No Encontrado"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Credenciales Incorrectas");
        }

        if (!user.isActive()){
            throw new RuntimeException("Usuario Inactivo");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getRole(),
            token
        );
    }
}
