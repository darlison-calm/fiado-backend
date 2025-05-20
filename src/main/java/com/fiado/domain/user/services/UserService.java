package com.fiado.domain.user.services;
import com.fiado.domain.user.dtos.UserRegisterDto;
import com.fiado.domain.user.entities.UserEntity;
import com.fiado.domain.user.enums.RoleType;
import com.fiado.domain.user.exception.DuplicateResourceException;
import com.fiado.domain.user.exception.UserNotFoundException;
import com.fiado.domain.user.mappers.UserMapper;
import com.fiado.domain.user.repositories.UserRepository;
import com.fiado.domain.user.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public UserDto registerUser(@NotNull UserRegisterDto dto) {
        Map<String, String> errors = new HashMap<>();
        if (userRepository.existsByEmail(dto.email())) {
            errors.put("email", "E-mail já está em uso");
        }
        if (userRepository.existsByUsername(dto.username())) {
            errors.put("username", "Nome de usuário já está em uso");
        }
        if (userRepository.existsByPhoneNumber(dto.phoneNumber())) {
            errors.put("phoneNumber", "Telefone já está em uso");
        }

        if(!errors.isEmpty()) throw new DuplicateResourceException(errors);
        String username = Optional.ofNullable(dto.username())
                .filter(s -> !s.isBlank())
                .orElse(null);
        UserEntity user = userMapper.toEntity(dto);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(RoleType.CUSTOMER);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserEntity getUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}