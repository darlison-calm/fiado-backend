package com.fiado.domain.user.dtos;

import com.fiado.domain.phone.PhoneNumberEntity;
import com.fiado.domain.phone.ValidPhone;
import com.fiado.domain.user.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;


@PasswordMatches
public record UserRegistrationRequest(

        @NotBlank(message = "Nome é obrigatório")
        String fullName,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Senha é obrigatório")
        String password,

        @NotBlank(message = "Confirmação de senha é obrigatório")
        String matchingPassword,

        String username,

        @ValidPhone PhoneNumberEntity phoneNumber)
{ }
