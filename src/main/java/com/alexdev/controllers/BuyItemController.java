package com.alexdev.controllers;

import com.alexdev.dto.response.BuyItemDTO;
import com.alexdev.services.BuyItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buyitems")
public class BuyItemController {

    private final BuyItemService service;

    public BuyItemController(BuyItemService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<BuyItemDTO>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuyItemDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }
}
