package com.fiado.domain.user.services;

import com.fiado.domain.user.errors.ConstraintsViolationException;
import com.fiado.domain.user.mappers.UserMapper;
import com.fiado.domain.user.repositories.UserRepository;
import com.fiado.domain.user.entities.UserEntity;
import com.fiado.domain.user.dtos.UserRegistrationRequest;
import com.fiado.domain.user.dtos.UserDto;
import com.fiado.domain.user.enums.RoleName;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public RegisterUserService(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto registerUser(@NotNull UserRegistrationRequest dto) {
        try {
            UserEntity user = userMapper.toEntity(dto);
            user.setPassword(passwordEncoder.encode(dto.password()));
            user.setRole(RoleName.ROLE_CUSTOMER);
            userRepository.save(user);
            return userMapper.toDto(user);
        } catch (DataIntegrityViolationException e) {
            Map<String, String> errors = getConstraintsErrorMessages(e);
            if (!errors.isEmpty()) {
                throw new ConstraintsViolationException(errors);
            }
            throw e;
        }
    }
    private Map<String, String> getConstraintsErrorMessages(DataIntegrityViolationException e) {
        String errorMessage = e.getMostSpecificCause().getMessage();
        Map<String, String> errors = new HashMap<>();

        if (errorMessage.contains("users_unique_email_idx")) {
            errors.put("email", "Email não disponível");
        }
        if (errorMessage.contains("users_unique_username_idx")) {
            errors.put("username", "Nome de usuário já existe");
        }
        if (errorMessage.contains("users_unique_phone_number_idx")) {
            errors.put("phoneNumber", "Telefone já cadastrado");
        }
        return errors;
    }
}