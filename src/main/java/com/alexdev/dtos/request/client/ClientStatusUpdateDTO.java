package com.alexdev.dtos.request.client;

import com.alexdev.domain.enums.ClientStatus;
import jakarta.validation.constraints.NotNull;


public record ClientStatusUpdateDTO(

        @NotNull(message = "Mandatory status!")
        ClientStatus status
){}
