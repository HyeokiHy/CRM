package com.example.b2bcrm.deal;

import javax.validation.constraints.NotBlank;

public class MoveDealRequest {

    @NotBlank
    private String direction;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

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
