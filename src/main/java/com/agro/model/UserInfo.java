package com.agro.model;

import com.agro.public_.enums.Role;

public record UserInfo(
        Integer id,
        String username,
        String password,
        Role role
) {
}