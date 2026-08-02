package com.alexdev.controllers;

import com.alexdev.dtos.request.client.ClientCreateDTO;
import com.alexdev.dtos.request.client.ClientStatusUpdateDTO;
import com.alexdev.dtos.request.client.ClientUpdateDTO;
import com.alexdev.dtos.response.client.ClientDetailsDTO;
import com.alexdev.dtos.response.client.ClientSummaryDTO;
import com.alexdev.services.ClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@Tag(
        name = "Clients",
        description = "Operations related to clients"
)
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientSummaryDTO>> findAllClients() {

        return ResponseEntity.ok(clientService.findAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDetailsDTO> findClientById(
            @Valid @PathVariable Long id) {

        return ResponseEntity.ok(clientService.findClientById(id));
    }

    @PostMapping
    public ResponseEntity<ClientDetailsDTO> createClient(
            @Valid @RequestBody ClientCreateDTO clientCreateDTO) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clientService.createClient(clientCreateDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDetailsDTO> updateClient(
            @Valid @PathVariable Long id,
            @Valid @RequestBody ClientUpdateDTO clientUpdateDTO
    ){

        return ResponseEntity.ok(clientService.updateClient(id, clientUpdateDTO));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ClientDetailsDTO> updateClientStatus(
            @Valid @PathVariable Long id,
            @Valid @RequestBody ClientStatusUpdateDTO clientStatusUpdateDTO
    ){

        return ResponseEntity.ok(clientService.updateClientStatus(id, clientStatusUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteClientById(@PathVariable Long id) {

        clientService.deleteClientById(id);

        return ResponseEntity.noContent().build();
    }
}
