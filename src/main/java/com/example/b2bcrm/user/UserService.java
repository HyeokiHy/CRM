package com.example.b2bcrm.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final AppUserRepository userRepository;

    public UserService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserRequest request) {
        String username = request.getUsername().trim();
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists.");
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(request.getPassword());
        user.setRole(AppUserRole.USER);
        return new UserResponse(userRepository.save(user));
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
