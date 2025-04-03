package com.fiado.domain.user;

import com.fiado.domain.user.dto.UserCreateDto;
import com.fiado.domain.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserEntity createUser(UserCreateDto dto) {
        UserEntity user = userMapper.toEntity(dto);
        return userRepository.save(user);
    }
}