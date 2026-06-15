package com.chat.bot_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bot_messages")
public class BotMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 2000)
    private String question;

    @Column(nullable = false, length = 8000)
    private String answer;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public BotMessage() {
    }

    public BotMessage(Long userId, String question, String answer, LocalDateTime createdAt) {
        this.userId = userId;
        this.question = question;
        this.answer = answer;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
