package com.chat.library_service.dto;

import com.chat.library_service.model.LibraryItemStatus;
import com.chat.library_service.model.LibraryItemType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateLibraryItemRequest {

    @NotBlank(message = "El título es obligatorio")
    private String title;

    @NotNull(message = "El tipo es obligatorio")
    private LibraryItemType type;

    @NotNull(message = "El estado es obligatorio")
    private LibraryItemStatus status;

    private String description;

    private String imageUrl;

    @Min(value = 0, message = "La calificación mínima es 0")
    @Max(value = 10, message = "La calificación máxima es 10")
    private Integer rating;

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