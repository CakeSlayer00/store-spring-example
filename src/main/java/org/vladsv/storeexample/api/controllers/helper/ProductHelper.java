package org.vladsv.storeexample.api.controllers.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.vladsv.storeexample.api.exceptions.NotFoundException;
import org.vladsv.storeexample.store.entities.ProductEntity;
import org.vladsv.storeexample.store.repositories.ProductRepository;

@RequiredArgsConstructor
@Component
public class ProductHelper {

    private final ProductRepository productRepository;

    public ProductEntity getProductOrThrowException(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Product with id: \"%s\" doesn't exist.",
                                        id
                                )
                        )
                );
    }

}
