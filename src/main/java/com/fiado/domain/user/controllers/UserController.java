package com.fiado.domain.user.controllers;

import com.fiado.domain.user.dtos.UserRegistrationRequest;
import com.fiado.domain.user.services.RegisterUserService;

import com.fiado.domain.user.dtos.UserDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    private final RegisterUserService registerUserService;

    @Autowired
    public UserController(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest user) {
        try {
            UserDto savedUser = registerUserService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Ocorreu um erro inesperado. Tente novamente.");
        }
    }

}
