package com.alexdev.mappers;

import com.alexdev.dto.response.ClientDTO;
import com.alexdev.entities.Client;

public class ClientMapper {

    public static ClientDTO entityToDTO(Client client) {
        if (client == null) {
            return null;
        }
        return new ClientDTO(client.getId(), client.getName());
    }
}
