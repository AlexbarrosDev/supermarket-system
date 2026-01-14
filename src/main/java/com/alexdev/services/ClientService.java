package com.alexdev.services;

import com.alexdev.dto.request.ClientCreateDTO;
import com.alexdev.dto.response.ClientDTO;

import com.alexdev.entities.Client;

import com.alexdev.exceptions.ResourceNotFoundException;
import com.alexdev.mappers.ClientMapper;

import com.alexdev.repositories.ClientRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(ClientMapper::entityToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Client entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not fond"));
        return ClientMapper.entityToDTO(entity);
    }

    @Transactional
    public ClientDTO create(ClientCreateDTO clientCreateDTO) {
        Client client = new Client(clientCreateDTO.name(), clientCreateDTO.cpf());
        return ClientMapper.entityToDTO(repository.save(client));
    }
}
