package com.chat.library_service.service;

import com.chat.library_service.dto.CreateLibraryItemRequest;
import com.chat.library_service.dto.LibraryItemResponse;
import com.chat.library_service.dto.UpdateLibraryItemRequest;
import com.chat.library_service.model.LibraryItem;
import com.chat.library_service.model.LibraryItemType;
import com.chat.library_service.repository.LibraryItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService {

    private final LibraryItemRepository libraryItemRepository;

    public LibraryService(LibraryItemRepository libraryItemRepository) {
        this.libraryItemRepository = libraryItemRepository;
    }

    public LibraryItemResponse createItem(
            Long userId,
            CreateLibraryItemRequest request
    ) {
        validateRating(request.getRating());

        LibraryItem item = new LibraryItem(
                userId,
                request.getTitle(),
                request.getType(),
                request.getStatus(),
                request.getDescription(),
                request.getImageUrl(),
                request.getRating()
        );

        LibraryItem savedItem = libraryItemRepository.save(item);

        return new LibraryItemResponse(savedItem);
    }

    public List<LibraryItemResponse> getMyItems(Long userId) {
        return libraryItemRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(LibraryItemResponse::new)
                .toList();
    }

    public LibraryItemResponse getItemById(Long userId, Long itemId) {
        LibraryItem item = findItemOrThrow(userId, itemId);
        return new LibraryItemResponse(item);
    }

    public List<LibraryItemResponse> getItemsByType(
            Long userId,
            LibraryItemType type
    ) {
        return libraryItemRepository
                .findByUserIdAndTypeOrderByCreatedAtDesc(userId, type)
                .stream()
                .map(LibraryItemResponse::new)
                .toList();
    }

    public List<LibraryItemResponse> searchByTitle(
            Long userId,
            String title
    ) {
        return libraryItemRepository
                .findByUserIdAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(userId, title)
                .stream()
                .map(LibraryItemResponse::new)
                .toList();
    }

    public LibraryItemResponse updateItem(
            Long userId,
            Long itemId,
            UpdateLibraryItemRequest request
    ) {
        validateRating(request.getRating());

        LibraryItem item = findItemOrThrow(userId, itemId);

        item.setTitle(request.getTitle());
        item.setType(request.getType());
        item.setStatus(request.getStatus());
        item.setDescription(request.getDescription());
        item.setImageUrl(request.getImageUrl());
        item.setRating(request.getRating());

        LibraryItem updatedItem = libraryItemRepository.save(item);

        return new LibraryItemResponse(updatedItem);
    }

    public void deleteItem(Long userId, Long itemId) {
        LibraryItem item = findItemOrThrow(userId, itemId);
        libraryItemRepository.delete(item);
    }

    private LibraryItem findItemOrThrow(Long userId, Long itemId) {
        return libraryItemRepository
                .findByIdAndUserId(itemId, userId)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
    }

    private void validateRating(Integer rating) {
        if (rating == null) {
            return;
        }

        if (rating < 0 || rating > 10) {
            throw new RuntimeException("La calificación debe estar entre 0 y 10");
        }
    }
}