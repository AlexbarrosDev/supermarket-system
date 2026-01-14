package com.alexdev.services;

import com.alexdev.dto.response.BuyItemDTO;
import com.alexdev.entities.BuyItem;
import com.alexdev.exceptions.ResourceNotFoundException;
import com.alexdev.mappers.BuyItemMapper;
import com.alexdev.repositories.BuyItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BuyItemService {

    private final BuyItemRepository repository;

    public BuyItemService(BuyItemRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<BuyItemDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(BuyItemMapper::entityToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public BuyItemDTO findById(Long id) {

        BuyItem entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BuyItem not found"));
        return BuyItemMapper.entityToDTO(entity);
    }
}
