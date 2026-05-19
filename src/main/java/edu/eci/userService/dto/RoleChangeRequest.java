package edu.eci.userService.dto;

import edu.eci.userService.enums.UserRole;

public class RoleChangeRequest {
    private UserRole newRole;

    public RoleChangeRequest() {
    }

    public RoleChangeRequest(UserRole newRole) {
        this.newRole = newRole;
    }

    public UserRole getNewRole() {
        return newRole;
    }

    public void setNewRole(UserRole newRole) {
        this.newRole = newRole;
    }
}
