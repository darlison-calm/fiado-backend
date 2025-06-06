package com.fiado.domain.clients.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClientResponseDto(
        Long id,
        String fullName,
        String address,
        String phoneNumber,
        UUID userId,
        String observation,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}