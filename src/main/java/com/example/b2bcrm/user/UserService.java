package com.example.b2bcrm.user;

import com.example.b2bcrm.user.dto.UserCreateRequest;
import com.example.b2bcrm.user.dto.UserResponse;
import com.example.b2bcrm.user.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final AppUserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(AppUserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponse createUser(UserCreateRequest request) {
        String username = request.getUsername().trim();
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists.");
        }

        AppUser user = userMapper.toEntity(request);
        return userMapper.toResponse(userRepository.save(user));
    }

    public AppUser authenticate(String username, String password) {
        AppUser user = userRepository.findByUsernameIgnoreCase(username.trim())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password."));

        if (!user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password.");
        }

        return user;
    }

    public boolean isAdmin(AppUser user) {
        return user.getRole() == AppUserRole.ADMIN;
    }

    public void ensureDefaultUsers() {
        ensureUser("Admin", "Admin", AppUserRole.ADMIN);
        ensureUser("J. Kim", "password", AppUserRole.USER);
        ensureUser("S. Lee", "password", AppUserRole.USER);
        ensureUser("M. Han", "password", AppUserRole.USER);
    }

    private void ensureUser(String username, String password, AppUserRole role) {
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            return;
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        userRepository.save(user);
    }
}
