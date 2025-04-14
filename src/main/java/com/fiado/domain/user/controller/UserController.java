package com.fiado.domain.user.controller;

import com.fiado.domain.authentication.AuthService;
import com.fiado.domain.user.dtos.UserLoginDto;
import com.fiado.domain.user.dtos.UserRegisterDto;
import com.fiado.domain.user.services.UserService;
import com.fiado.domain.user.dtos.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserRegisterDto user) {
        UserDto savedUser = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping
    public String getMessage() {
        return "Msg secreta";
    }

    @PostMapping("/auth")
    public String authenticate(@RequestBody UserLoginDto dto) {
        return authService.authenticate(dto.login(), dto.password());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserInfo(Authentication authentication) {
        UUID userId = authService.getUserFromSession(authentication);
        return ResponseEntity.ok("User ID: " + userId);
    }
}
