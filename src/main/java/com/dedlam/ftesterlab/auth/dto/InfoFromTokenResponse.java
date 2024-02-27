package com.dedlam.ftesterlab.auth.dto;

import com.dedlam.ftesterlab.auth.models.Role;

import java.util.Set;

public record InfoFromTokenResponse(
        String username,
        Set<Role> roles
) {
}