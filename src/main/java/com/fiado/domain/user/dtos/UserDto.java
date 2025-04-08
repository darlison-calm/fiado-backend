package com.fiado.domain.user.dtos;

import com.fiado.domain.phone.PhoneNumberEntity;
import com.fiado.domain.user.enums.RoleName;

import java.util.UUID;


public record UserDto(
        UUID id,

        String fullName,

        String email,

        String username,

        PhoneNumberEntity phoneNumber,

        RoleName role
) {}