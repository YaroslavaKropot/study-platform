package com.studyplatform.client.dto;

public class RegisterRequest {
    private String name;
    private String password;
    private String email;

    public RegisterRequest() {}

    public RegisterRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "RegisterRequest{name='" + name + "', email='" + email + "'}";
    }
}