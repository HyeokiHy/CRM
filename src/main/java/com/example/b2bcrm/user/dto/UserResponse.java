package com.example.b2bcrm.user.dto;

import com.example.b2bcrm.user.AppUserRole;

public class UserResponse {

    private final Long id;
    private final String username;
    private final AppUserRole role;

    public UserResponse(Long id, String username, AppUserRole role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public AppUserRole getRole() {
        return role;
    }
}
