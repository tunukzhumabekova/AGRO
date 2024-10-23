package com.agro.model.request;

import com.agro.public_.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignUp(
        @NotBlank(message = "Username is mandatory")
        String username,
        @NotBlank(message = "Password is mandatory")
        String password,
        @NotNull(message = "Role is mandatory")
        Role role
){}
