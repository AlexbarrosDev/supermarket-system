package com.alexdev.services;

import com.alexdev.domain.entities.Address;
import com.alexdev.dtos.request.client.ClientCreateDTO;
import com.alexdev.dtos.request.client.ClientStatusUpdateDTO;
import com.alexdev.dtos.request.client.ClientUpdateDTO;
import com.alexdev.dtos.response.client.ClientDetailsDTO;
import com.alexdev.domain.entities.Client;
import com.alexdev.dtos.response.client.ClientSummaryDTO;
import com.alexdev.exceptions.BusinessException;
import com.alexdev.exceptions.ResourceNotFoundException;
import com.alexdev.mappers.AddressMapper;
import com.alexdev.mappers.ClientMapper;
import com.alexdev.repositories.ClientRepository;
import com.alexdev.repositories.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    private final SaleRepository saleRepository;

    private final ClientMapper clientMapper;

    private final AddressMapper addressMapper;

    @Transactional(readOnly = true)
    public List<ClientSummaryDTO> findAllClients() {

        List<Client> clientList = clientRepository.findAll();

        if (clientList.isEmpty()) {
            throw new ResourceNotFoundException("Client not found");
        }

        return clientMapper
                .clientEntityListToClientSummaryDTOList(clientList);
    }

    @Transactional(readOnly = true)
    public ClientDetailsDTO findClientById(Long id) {

        Client client = clientRepository
                .findById(id)
                .orElseThrow(()
                -> new ResourceNotFoundException("Client not found"));

        return clientMapper.clientEntityToClientDetailsDTO(client);
    }

    @Transactional
    public ClientDetailsDTO createClient(ClientCreateDTO clientCreateDTO) {

        validateCpfUniqueness(clientCreateDTO.cpf());

        Client entity = clientMapper.clientRequestDTOToClientEntity(clientCreateDTO);

        Address address = addressMapper
                .addressRequestDTOToAddressEntity(clientCreateDTO.address());

        entity.setAddress(address);

        return clientMapper
                .clientEntityToClientDetailsDTO(clientRepository.save(entity));
    }

    @Transactional
    public ClientDetailsDTO updateClient(Long id, ClientUpdateDTO clientUpdateDTO) {

        Client client = clientRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        Address address = client.getAddress();

        address.setStreet(clientUpdateDTO.address().street());
        address.setCity(clientUpdateDTO.address().city());
        address.setState(clientUpdateDTO.address().state());
        address.setNumber(clientUpdateDTO.address().number());
        address.setZip(clientUpdateDTO.address().zip());

        client.setName(clientUpdateDTO.name());
        client.setPhone(clientUpdateDTO.phone());
        client.setAddress(address);

        return clientMapper
                .clientEntityToClientDetailsDTO(client);
    }

    @Transactional
    public ClientDetailsDTO updateClientStatus(Long id, ClientStatusUpdateDTO clientStatusUpdateDTO) {

        Client client = clientRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        if (client.getStatus() == clientStatusUpdateDTO.status()) {
            throw new BusinessException("Client is already in the requested status.");
        }

        client.setStatus(clientStatusUpdateDTO.status());

        return clientMapper
                .clientEntityToClientDetailsDTO(client);
    }

    @Transactional
    public void deleteClientById(Long id) {

        Client client = clientRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        if (saleRepository.existsByClientId(client.getId())) {

            throw new BusinessException("Client cannot be deleted because it has associated sales.");
        }

        clientRepository.delete(client);
    }

    private void validateCpfUniqueness(String cpf) {

        if (clientRepository.existsByCpf(cpf)) {
            throw new BusinessException("A client with this CPF already exists.");
        }
    }
}
