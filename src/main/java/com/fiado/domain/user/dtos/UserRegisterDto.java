package com.fiado.domain.user.dtos;

import com.fiado.domain.phone.PhoneNumberEntity;
import com.fiado.domain.phone.ValidPhone;
import com.fiado.domain.user.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


@PasswordMatches
public record UserRegisterDto(

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Senha é obrigatório")
        String password,

        @NotBlank(message = "Confirmação de senha é obrigatório")
        String matchingPassword,

        @Pattern(regexp = "^[a-zA-Z0-9_-]{3,20}$|^$", message = "Nome de usuário deve ter entre 3 e 20 caracteres, contendo apenas letras, números, underscores ou hífens, ou ser vazio")
        String username,

        @ValidPhone PhoneNumberEntity phoneNumber)
{ }
