package com.example.b2bcrm.user;

import java.net.URI;
import jakarta.validation.Valid;
import com.example.b2bcrm.user.dto.UserCreateRequest;
import com.example.b2bcrm.user.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@SuppressWarnings("unused")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse created = userService.createUser(request);
        return ResponseEntity
            .created(URI.create("/api/users/" + created.getId()))
            .body(created);
    }
}
