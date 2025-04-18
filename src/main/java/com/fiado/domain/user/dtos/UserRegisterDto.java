package com.fiado.domain.user.dtos;

import com.fiado.domain.phone.PhoneNumberEntity;
import com.fiado.domain.phone.ValidPhone;
import com.fiado.domain.user.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@PasswordMatches
public record UserRegisterDto(

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
