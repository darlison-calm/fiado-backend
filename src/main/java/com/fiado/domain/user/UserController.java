package com.fiado.domain.user;

import com.fiado.domain.user.dto.UserCreateDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    ResponseEntity<String> addUser(@Valid @RequestBody UserCreateDto user) {
        UserEntity savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser.toString());
    }

    @GetMapping
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello, World!");
    }
}
