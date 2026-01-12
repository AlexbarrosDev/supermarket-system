package com.alexdev.controllers;

import com.alexdev.dto.request.MarkCreateDTO;
import com.alexdev.dto.response.MarkDTO;
import com.alexdev.services.MarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marks")
public class MarkController {

    private final MarkService service;

    public MarkController(MarkService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MarkDTO>> findAll() {

        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarkDTO> findById(@PathVariable Long id) {

        return ResponseEntity.ok().body(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<MarkDTO> create(@RequestBody MarkCreateDTO markCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(markCreateDTO));
    }
}
