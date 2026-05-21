package edu.eci.userService.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class RoleChangeRequest {
    @JsonAlias("systemRole")
    private String role;

    public RoleChangeRequest() {
    }

    public RoleChangeRequest(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
