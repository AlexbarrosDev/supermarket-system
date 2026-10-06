package com.alexdev.dtos.request.user;

import com.alexdev.domain.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterDTO(

        @Email
        String login,

        @NotBlank
        @Size(min = 6)
        String password,

        @NotNull(message = "Mandatory role!")
        UserRole role
){}
