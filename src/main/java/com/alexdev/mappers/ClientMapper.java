package com.alexdev.mappers;

import com.alexdev.dtos.request.client.ClientCreateDTO;
import com.alexdev.dtos.response.client.ClientDetailsDTO;
import com.alexdev.domain.entities.Client;
import com.alexdev.dtos.response.client.ClientSummaryDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface ClientMapper {

    Client clientRequestDTOToClientEntity(ClientCreateDTO clientCreateDTO);

    ClientDetailsDTO clientEntityToClientDetailsDTO(Client client);

    ClientSummaryDTO clientEntityToClientSummaryDTO(Client client);

    List<Client> clientCreateDTOListToClientList(List<ClientCreateDTO> clients);

    List<ClientDetailsDTO> clientEntityListToClientDetailsDTOList(List<Client> clients);

    List<ClientSummaryDTO> clientEntityListToClientSummaryDTOList(List<Client> clients);
}
