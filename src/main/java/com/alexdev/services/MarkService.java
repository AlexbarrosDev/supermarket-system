package com.alexdev.services;

import com.alexdev.dto.request.MarkCreateDTO;
import com.alexdev.dto.response.MarkDTO;

import com.alexdev.entities.Mark;

import com.alexdev.exceptions.BusinessException;
import com.alexdev.exceptions.ResourceNotFoundException;
import com.alexdev.mappers.MarkMapper;

import com.alexdev.repositories.MarkRepository;

import com.alexdev.repositories.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class MarkService {

    private final MarkRepository markRepository;
    private final ProductRepository productRepository;

    public MarkService(MarkRepository markRepository, ProductRepository productRepository) {
        this.markRepository = markRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<MarkDTO> findAll() {
        return markRepository.findAll()
                .stream()
                .map(MarkMapper::entityToDTO).toList();
    }

    @Transactional(readOnly = true)
    public MarkDTO findById(Long id) {
        Mark entity = markRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Mark not found"));
        return MarkMapper.entityToDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        markRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mark not found"));

        if (productRepository.existsByMarkId(id)) {
            throw new BusinessException(
                    "Cannot delete mark: it is being used by products");
        }
        markRepository.deleteById(id);
    }

    @Transactional
    public MarkDTO create(MarkCreateDTO markCreateDTO) {
        if (markCreateDTO.name() == null || markCreateDTO.name().isBlank()) {
            throw new BusinessException("Mark name must not be null or blank");
        }

        Mark entity = new Mark(markCreateDTO.name());
        return MarkMapper.entityToDTO(markRepository.save(entity));
    }
}
