package com.alexdev.dtos.request.group;

import jakarta.validation.constraints.NotBlank;

public record GroupUpdateDTO(

        @NotBlank(message = "Mandatory Group name!")
        String name
){
}
