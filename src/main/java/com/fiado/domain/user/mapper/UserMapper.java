package com.fiado.domain.user.mapper;

import com.fiado.domain.user.UserEntity;
import com.fiado.domain.user.dto.UserCreateDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(UserCreateDto dto);
//    UserResponseDto toDto(UserEntity entity);
}