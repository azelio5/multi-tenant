package com.anvar.saas.mappers;

import com.anvar.saas.entities.Category;
import com.anvar.saas.requests.CategoryRequest;
import com.anvar.saas.responses.CategoryResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class CategoryMapper {

    public Category toEntity(final CategoryRequest request) {
        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .deleted(false)
                .build();
    }

    public CategoryResponse toResponse(final Category entity) {
        final int nbProduct = 0;// entity.getProducts() == null ? 0 : entity.getProducts().size();
        return CategoryResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .nbProducts(nbProduct)
                .build();
    }
}
