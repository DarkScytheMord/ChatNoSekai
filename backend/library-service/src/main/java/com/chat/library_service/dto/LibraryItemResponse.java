package com.chat.library_service.dto;

import com.chat.library_service.model.LibraryItem;
import com.chat.library_service.model.LibraryItemStatus;
import com.chat.library_service.model.LibraryItemType;

import java.time.LocalDateTime;

public class LibraryItemResponse {

    private Long id;
    private Long userId;
    private String title;
    private LibraryItemType type;
    private LibraryItemStatus status;
    private String description;
    private String imageUrl;
    private Integer rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LibraryItemResponse(LibraryItem item) {
        this.id = item.getId();
        this.userId = item.getUserId();
        this.title = item.getTitle();
        this.type = item.getType();
        this.status = item.getStatus();
        this.description = item.getDescription();
        this.imageUrl = item.getImageUrl();
        this.rating = item.getRating();
        this.createdAt = item.getCreatedAt();
        this.updatedAt = item.getUpdatedAt();
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
}