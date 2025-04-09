package com.fiado.domain.user.mappers;

import com.fiado.domain.user.dtos.UserRegistrationRequest;
import com.fiado.domain.user.UserEntity;
import org.mapstruct.Mapper;
import com.fiado.domain.user.dtos.UserDto;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(UserRegistrationRequest dto);
    UserDto toDto(UserEntity entity);
}