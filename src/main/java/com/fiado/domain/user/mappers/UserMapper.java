package com.fiado.domain.user.mappers;

import com.fiado.domain.user.dtos.UserRegisterDto;
import com.fiado.domain.user.entities.UserEntity;

import org.mapstruct.Mapper;
import com.fiado.domain.user.dtos.UserDto;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(UserRegisterDto dto);
    UserDto toDto(UserEntity entity);
}