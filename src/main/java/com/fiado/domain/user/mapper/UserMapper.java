package com.fiado.domain.user.mapper;

import com.fiado.domain.user.UserEntity;
import com.fiado.domain.user.dto.CreateUserDto;
import com.fiado.domain.user.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(CreateUserDto dto);
    UserDto toDto(UserEntity entity);
}