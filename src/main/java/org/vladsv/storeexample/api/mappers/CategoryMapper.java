package org.vladsv.storeexample.api.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.vladsv.storeexample.api.dto.CategoryDTO;
import org.vladsv.storeexample.store.entities.CategoryEntity;

//Shall i make it static?
@RequiredArgsConstructor
@Component
public class CategoryMapper {

    private final ProductMapper productMapper;

    public CategoryDTO map(CategoryEntity category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .products(category
                        .getProducts()
                        .stream()
                        .map(productMapper::map)
                        .toList())
                .build();
    }
}
