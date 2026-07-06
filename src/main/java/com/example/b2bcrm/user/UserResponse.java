package com.example.b2bcrm.user;

public class UserResponse {

    private final Long id;
    private final String username;
    private final AppUserRole role;

    public UserResponse(AppUser user) {
        id = user.getId();
        username = user.getUsername();
        role = user.getRole();
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
