package com.anvar.saas.services.impl;

import com.anvar.saas.entities.Category;
import com.anvar.saas.mappers.CategoryMapper;
import com.anvar.saas.repositories.CategoryRepository;
import com.anvar.saas.requests.CategoryRequest;
import com.anvar.saas.responses.CategoryResponse;
import com.anvar.saas.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    public final CategoryMapper categoryMapper;


    @Override
    public void create(CategoryRequest request) {
        //char if exists
        checkIfCategoryExistsByName(request.getName());

        final Category category = categoryMapper.toEntity(request);
        categoryRepository.save(category);
    }

    @Override
    public void update(String id, CategoryRequest request) {
        final Optional<Category> existingCategory = this.categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            log.debug("Category not found");
            throw new EntityNotFoundException("Category not found");
        }

        final Category category = existingCategory.get();
        if (!category.getName().equals(request.getName())) {
            checkIfCategoryExistsByName(request.getName());
        }

        Category updatedCategory = categoryMapper.toEntity(request);
        updatedCategory.setId(id);
        categoryRepository.save(updatedCategory);


    }

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream().map(categoryMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public CategoryResponse findById(String id) {
        return this.categoryRepository.findById(id).map(categoryMapper::toResponse).orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    @Override
    public void delete(String id) {
        Category category = this.categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found"));
        this.categoryRepository.delete(category);
    }

    private void checkIfCategoryExistsByName(String name) {
        final Optional<Category> category = this.categoryRepository.findByNameIgnoreCase(name);

        if (category.isPresent()) {
            log.debug("Category {} exists", name);
            throw new RuntimeException("Category " + name + " already exists");
        }
    }
}
