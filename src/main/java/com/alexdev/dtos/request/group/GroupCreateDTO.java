package com.alexdev.dtos.request.group;

import jakarta.validation.constraints.NotBlank;

public record GroupCreateDTO(

        @NotBlank(message = "Mandatory Group name!")
        String name
){
}
