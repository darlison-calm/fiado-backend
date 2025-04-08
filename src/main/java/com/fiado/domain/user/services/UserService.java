package com.fiado.domain.user.services;

import com.fiado.domain.infra.ConstraintsViolationException;
import com.fiado.domain.user.exception.UserConstraintErrorHandler;
import com.fiado.domain.user.mappers.UserMapper;
import com.fiado.domain.user.repositories.UserRepository;
import com.fiado.domain.user.entities.UserEntity;
import com.fiado.domain.user.dtos.UserRegistrationRequest;
import com.fiado.domain.user.dtos.UserDto;
import com.fiado.domain.user.enums.RoleName;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserDto registerUser(@NotNull UserRegistrationRequest dto) {
        try {
            UserEntity user = userMapper.toEntity(dto);
            user.setPassword(passwordEncoder.encode(dto.password()));
            user.setRole(RoleName.ROLE_CUSTOMER);
            userRepository.save(user);
            return userMapper.toDto(user);
        } catch (DataIntegrityViolationException e) {
            Map<String, String> errors = UserConstraintErrorHandler.parse(e);
            if (!errors.isEmpty()) {
                throw new ConstraintsViolationException(errors);
            }
            throw e;
        }
    }

}