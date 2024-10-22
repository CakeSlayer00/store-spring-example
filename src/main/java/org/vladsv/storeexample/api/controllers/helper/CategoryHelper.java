package org.vladsv.storeexample.api.controllers.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.vladsv.storeexample.api.exceptions.NotFoundException;
import org.vladsv.storeexample.store.entities.CategoryEntity;
import org.vladsv.storeexample.store.repositories.CategoryRepository;

@RequiredArgsConstructor
@Component
public class CategoryHelper {

    private final CategoryRepository categoryRepository;

    public CategoryEntity getCategoryOrThrowException(Long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Category with id: \"%s\" doesn't exist.",
                                        id
                                )
                        )
                );
    }
}
