package org.vladsv.storeexample.api.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.vladsv.storeexample.api.dto.ProductDTO;
import org.vladsv.storeexample.store.entities.ProductEntity;

@RequiredArgsConstructor
@Component
public class ProductMapper {

    private final ReviewMapper reviewMapper;

    public ProductDTO map(ProductEntity product) {

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .reviews(product
                        .getReviews()
                        .stream()
                        .map(reviewMapper::map)
                        .toList())
                .build();
    }
}
