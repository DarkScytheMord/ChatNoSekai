package com.chat.library_service.repository;

import com.chat.library_service.model.LibraryItem;
import com.chat.library_service.model.LibraryItemType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibraryItemRepository extends JpaRepository<LibraryItem, Long> {

    List<LibraryItem> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<LibraryItem> findByUserIdAndTypeOrderByCreatedAtDesc(
            Long userId,
            LibraryItemType type
    );

    List<LibraryItem> findByUserIdAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(
            Long userId,
            String title
    );

    Optional<LibraryItem> findByIdAndUserId(Long id, Long userId);
}