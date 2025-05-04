package com.fiado.domain.clients.dto;

import jakarta.validation.constraints.NotBlank;

public record ClientRequestDto(

        @NotBlank(message = "O nome é obrigatório")
        String fullName,

        String address,
        String phoneNumber,
        String observation
) {
}