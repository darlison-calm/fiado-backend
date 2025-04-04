package com.fiado.domain.user.dto;

import com.fiado.domain.phone.PhoneNumberEntity;

import java.util.UUID;


public record UserDto(
        UUID id,

        String fullName,

        String email,

        String username,

        PhoneNumberEntity phoneNumber
) {}