package com.alexdev.dtos.request.user;

import jakarta.validation.constraints.NotBlank;

public record AuthenticateDTO(

        @NotBlank
        String login,

        @NotBlank
        String password
)
{}
