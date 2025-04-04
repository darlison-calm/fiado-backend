package com.fiado.domain.user;

import com.fiado.domain.phone.PhoneNumberEntity;
import com.fiado.domain.user.dto.CreateUserDto;
import com.fiado.domain.user.dto.UserDto;
import com.fiado.domain.user.error.EmailAlreadyRegisteredException;
import com.fiado.domain.user.error.PhoneAlreadyRegisteredException;
import com.fiado.domain.user.mapper.UserMapper;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto createUser(@NotNull CreateUserDto dto) {
        if (hasUserWithEmail(dto.email())) {
            throw new EmailAlreadyRegisteredException();
        }

        if (hasUserWithPhoneNumber(dto.phoneNumber())){
            throw new PhoneAlreadyRegisteredException();
        }
        UserEntity user = userMapper.toEntity(dto);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    private boolean hasUserWithEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean hasUserWithPhoneNumber(PhoneNumberEntity phone){
        return userRepository.existsByPhoneNumber(phone);
    }
}