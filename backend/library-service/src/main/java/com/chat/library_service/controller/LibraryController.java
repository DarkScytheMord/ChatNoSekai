package com.chat.library_service.controller;

import com.chat.library_service.dto.CreateLibraryItemRequest;
import com.chat.library_service.dto.LibraryItemResponse;
import com.chat.library_service.dto.UpdateLibraryItemRequest;
import com.chat.library_service.model.LibraryItemType;
import com.chat.library_service.service.JwtService;
import com.chat.library_service.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library/items")
public class LibraryController {

    private final LibraryService libraryService;
    private final JwtService jwtService;

    public LibraryController(
            LibraryService libraryService,
            JwtService jwtService
    ) {
        this.libraryService = libraryService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<LibraryItemResponse> createItem(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody CreateLibraryItemRequest request
    ) {
        Long userId = jwtService.extractUserIdFromAuthorizationHeader(authorizationHeader);
        LibraryItemResponse response = libraryService.createItem(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<LibraryItemResponse>> getMyItems(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        Long userId = jwtService.extractUserIdFromAuthorizationHeader(authorizationHeader);
        List<LibraryItemResponse> response = libraryService.getMyItems(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<LibraryItemResponse> getItemById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long itemId
    ) {
        Long userId = jwtService.extractUserIdFromAuthorizationHeader(authorizationHeader);
        LibraryItemResponse response = libraryService.getItemById(userId, itemId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<LibraryItemResponse>> getItemsByType(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable LibraryItemType type
    ) {
        Long userId = jwtService.extractUserIdFromAuthorizationHeader(authorizationHeader);
        List<LibraryItemResponse> response = libraryService.getItemsByType(userId, type);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<LibraryItemResponse>> searchByTitle(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String title
    ) {
        Long userId = jwtService.extractUserIdFromAuthorizationHeader(authorizationHeader);
        List<LibraryItemResponse> response = libraryService.searchByTitle(userId, title);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<LibraryItemResponse> updateItem(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateLibraryItemRequest request
    ) {
        Long userId = jwtService.extractUserIdFromAuthorizationHeader(authorizationHeader);
        LibraryItemResponse response = libraryService.updateItem(userId, itemId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long itemId
    ) {
        Long userId = jwtService.extractUserIdFromAuthorizationHeader(authorizationHeader);
        libraryService.deleteItem(userId, itemId);
        return ResponseEntity.noContent().build();
    }
}