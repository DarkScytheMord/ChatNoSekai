package com.chat.library_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "library_items")
public class LibraryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LibraryItemType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LibraryItemStatus status;

    @Column(length = 1000)
    private String description;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String imageUrl;

    private Integer rating;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public LibraryItem() {
    }

    public LibraryItem(
            Long userId,
            String title,
            LibraryItemType type,
            LibraryItemStatus status,
            String description,
            String imageUrl,
            Integer rating
    ) {
        this.userId = userId;
        this.title = title;
        this.type = type;
        this.status = status;
        this.description = description;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = LibraryItemStatus.PENDING;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public LibraryItemType getType() {
        return type;
    }

    public LibraryItemStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getRating() {
        return rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(LibraryItemType type) {
        this.type = type;
    }

    public void setStatus(LibraryItemStatus status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}