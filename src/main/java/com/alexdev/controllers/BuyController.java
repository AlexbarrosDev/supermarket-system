package com.alexdev.controllers;

import com.alexdev.dto.request.BuyCreateDTO;
import com.alexdev.dto.response.BuyDTO;
import com.alexdev.services.BuyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buys")
public class BuyController {

    private final BuyService service;

    public BuyController(BuyService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<BuyDTO>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuyDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<BuyDTO> create(@RequestBody BuyCreateDTO buyCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(buyCreateDTO));
    }
}
