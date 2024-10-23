package com.agro.model.response;

import com.agro.public_.enums.Role;
import lombok.Builder;

@Builder
public record Authentication(
        int id,
        String username,
        String token,
        Role role

) {
}

