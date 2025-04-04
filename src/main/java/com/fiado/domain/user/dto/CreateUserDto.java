package com.fiado.domain.user.dto;

import com.fiado.domain.phone.PhoneNumberEntity;
import com.fiado.domain.phone.ValidPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record CreateUserDto(
        @NotBlank(message = "Nome é obrigatório")
        String fullName,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Senha é obrigatório")
        String password,

        String username,

        @ValidPhone
        PhoneNumberEntity phoneNumber
) {}