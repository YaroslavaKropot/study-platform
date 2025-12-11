package com.studyplatform.client.dto;

import com.studyplatform.client.model.User;

public class LoginResponse {
    private User user;

    public LoginResponse() {}

    public LoginResponse(User user) {
        this.user = user;
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        return "LoginResponse{user=" + user + "}";
    }
}