package com.fiado.domain.clients.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClientDto(
        Long id,
        String fullName,
        String address,
        String phoneNumber,
        UUID userId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}