package com.alexdev.controllers;

import com.alexdev.dto.request.ClientCreateDTO;
import com.alexdev.dto.response.ClientDTO;
import com.alexdev.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ClientDTO> create(@RequestBody ClientCreateDTO clientCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(clientCreateDTO));
    }
}
