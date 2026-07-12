package com.alexdev.controllers;

import com.alexdev.dtos.request.category.CategoryCreateDTO;
import com.alexdev.dtos.request.category.CategoryUpdateDTO;
import com.alexdev.dtos.response.category.CategoryDetailsDTO;
import com.alexdev.services.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(
        name = "Categories",
        description = "Operations related to categories"
)
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDetailsDTO>> findAllCategories() {

        return ResponseEntity.ok(categoryService.findAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDetailsDTO> findCategoryById(
            @Valid @PathVariable Long id) {

        return ResponseEntity.ok().body(categoryService.findCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryDetailsDTO> createCategory(
            @Valid @RequestBody CategoryCreateDTO categoryCreateDTO) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.createCategory(categoryCreateDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDetailsDTO> updateCategory(
            @Valid @PathVariable Long id,
            @RequestBody CategoryUpdateDTO categoryUpdateDTO)
    {
        return ResponseEntity
                .ok()
                .body(categoryService.updateCategory(id, categoryUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {

        categoryService.deleteCategoryById(id);

        return ResponseEntity.noContent().build();
    }
}
