package com.alexdev.dtos.response.client;

import com.alexdev.domain.enums.ClientStatus;
import com.alexdev.dtos.response.address.AddressDetailsDTO;

import java.time.Instant;

public record ClientDetailsDTO(

        Long id,
        String name,
        String cpf,
        ClientStatus status,
        String phone,
        Instant registrationDate,
        AddressDetailsDTO address
) {}
