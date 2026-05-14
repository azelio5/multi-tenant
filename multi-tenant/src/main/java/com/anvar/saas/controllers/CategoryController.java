package com.anvar.saas.controllers;

import com.anvar.saas.requests.CategoryRequest;
import com.anvar.saas.responses.CategoryResponse;
import com.anvar.saas.services.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<Void> createCategory(
            @RequestBody
            @Valid
            final CategoryRequest request
    ) {
        this.service.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(){
        return ResponseEntity.ok(this.service.findAll());
    }


    @PutMapping("/{category-id}")
    public ResponseEntity<Void> updateCategory(
            @RequestBody
            @Valid
            final CategoryRequest request,
            @PathVariable("category-id")
            @NotNull(message = "Category ID cannot be null")
            final String id
    ) {
        this.service.update(id, request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{category-id}")
    public ResponseEntity<CategoryResponse> findCategoryById(
            @PathVariable("category-id")
            @NotNull(message = "Category ID cannot be null")
            final String id
    ) {
        return ResponseEntity.ok(this.service.findById(id));
    }


    @DeleteMapping("/{category-id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable("category-id")
            @NotNull(message = "Category ID cannot be null")
            final String id
    ) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

}