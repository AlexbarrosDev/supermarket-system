package com.alexdev.dtos.request.client;

import com.alexdev.domain.client.ClientStatus;
import com.alexdev.dtos.request.address.AddressCreateDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record ClientCreateDTO(

        @NotBlank(message = "Mandatory name!")
        String name,

        @CPF(message = "Invalid CPF!")
        String cpf,

        @NotNull(message = "Mandatory status!")
        ClientStatus status,

        String phone,

        AddressCreateDTO address
        ) {
}
