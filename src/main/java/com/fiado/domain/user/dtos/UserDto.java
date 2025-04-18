package com.fiado.domain.user.dtos;

import com.fiado.domain.phone.PhoneNumberEntity;
import com.fiado.domain.user.enums.RoleType;

import java.util.UUID;


public record UserDto(
        UUID id,

        String email,

        String username,

        PhoneNumberEntity phoneNumber,

        RoleType role
) {}