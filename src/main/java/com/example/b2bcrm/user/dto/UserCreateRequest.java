package com.example.b2bcrm.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserCreateRequest {

    @NotBlank
    @Size(max = 60)
    private String username;

    @NotBlank
    @Size(max = 120)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
