package com.dedlam.ftesterlab.auth.dto;

public record CreateUserRequest(
        UserType userType,
        String username,
        String password
) {
    public enum UserType {
        STUDENT,
        TEACHER
    }
}