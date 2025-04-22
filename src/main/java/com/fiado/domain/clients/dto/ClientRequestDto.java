package com.fiado.domain.clients.dto;

public record ClientRequestDto(
        String fullName,
        String address,
        String phoneNumber
) {
}