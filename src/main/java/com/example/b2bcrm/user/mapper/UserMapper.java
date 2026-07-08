package com.example.b2bcrm.user.mapper;

import com.example.b2bcrm.user.AppUser;
import com.example.b2bcrm.user.AppUserRole;
import com.example.b2bcrm.user.dto.UserCreateRequest;
import com.example.b2bcrm.user.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public AppUser toEntity(UserCreateRequest request) {
        AppUser user = new AppUser();
        user.setUsername(request.getUsername().trim());
        user.setPassword(request.getPassword());
        user.setRole(AppUserRole.USER);
        return user;
    }

    public UserResponse toResponse(AppUser user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }
}
