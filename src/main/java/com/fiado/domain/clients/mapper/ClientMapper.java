package com.fiado.domain.clients.mapper;

import com.fiado.domain.clients.ClientEntity;
import com.fiado.domain.clients.dto.ClientRequestDto;
import com.fiado.domain.clients.dto.ClientResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    @Mapping(target = "userId", source = "user.id")
    ClientResponseDto toDto(ClientEntity entity);

    ClientEntity toEntity(ClientRequestDto dto);
}
