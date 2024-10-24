package org.vladsv.storeexample.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.vladsv.storeexample.api.controllers.helper.CategoryHelper;
import org.vladsv.storeexample.api.controllers.helper.ProductHelper;
import org.vladsv.storeexample.api.dto.AskDTO;
import org.vladsv.storeexample.api.dto.ProductDTO;
import org.vladsv.storeexample.api.exceptions.BadRequestException;
import org.vladsv.storeexample.api.mappers.ProductMapper;
import org.vladsv.storeexample.store.entities.CategoryEntity;
import org.vladsv.storeexample.store.entities.ProductEntity;
import org.vladsv.storeexample.store.repositories.CategoryRepository;
import org.vladsv.storeexample.store.repositories.ProductRepository;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper;

    private final ProductHelper productHelper;

    private final CategoryHelper categoryHelper;

    public static final String CREATE_PRODUCT = "api/v1/{category_id}/products";
    public static final String GET_PRODUCT = "api/v1/products/{id}";
    public static final String GET_ALL_PRODUCTS = "api/v1/categories/{category_id}/products";
    public static final String UPDATE_PRODUCT = "api/v1/{category_id}/products/{id}";
    public static final String DELETE_PRODUCT = "api/v1/products/{id}";

    @PostMapping(CREATE_PRODUCT)
    public ProductDTO createProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Double price,
            @PathVariable("category_id") Long categoryId
    ) {
        if (name.trim().isEmpty()) {
            throw new BadRequestException("Product name cannot be empty");
        }

        if (description.trim().isEmpty()) {
            throw new BadRequestException("Description cannot be empty");
        }

        if (price.isNaN()) {
            throw new BadRequestException("Price cannot be NaN");
        }

        CategoryEntity category = categoryHelper.getCategoryOrThrowException(categoryId);

        ProductEntity product = ProductEntity.builder()
                .name(name)
                .description(description)
                .price(price)
                .build();

        if (checkWhetherProductBelongsToCategory(product, category)) {
            throw new BadRequestException("Product already exists");
        }

        productRepository.saveAndFlush(product);

        category.getProducts().add(product);
        categoryRepository.saveAndFlush(category);

        return productMapper.map(product);

    }

    @GetMapping(GET_PRODUCT)
    public ProductDTO getProduct(
            @PathVariable("id") Long id
    ) {

        ProductEntity product = productHelper.getProductOrThrowException(id);

        return productMapper.map(product);
    }

    @GetMapping(GET_ALL_PRODUCTS)
    public List<ProductDTO> getAllProducts(
            @PathVariable("category_id") Long categoryId
    ) {

        return categoryHelper.getCategoryOrThrowException(categoryId)
                .getProducts()
                .stream()
                .map(productMapper::map)
                .toList();
    }

    @PutMapping(UPDATE_PRODUCT)
    public ProductDTO updateProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Double price,
            @PathVariable("id") Long id,
            @PathVariable("category_id") Long categoryId
    ) {

        if (name.trim().isEmpty()) {
            throw new BadRequestException("Product name cannot be empty");
        }

        if (description.trim().isEmpty()) {
            throw new BadRequestException("Description cannot be empty");
        }

        if (price.isNaN()) {
            throw new BadRequestException("Price cannot be NaN");
        }

        ProductEntity product = productHelper.getProductOrThrowException(id);
        CategoryEntity category = categoryHelper.getCategoryOrThrowException(categoryId);

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        productRepository.saveAndFlush(product);

        if (!checkWhetherProductBelongsToCategory(product, category)) {
            category.getProducts().add(product);
        }

        categoryRepository.saveAndFlush(category);

        return productMapper.map(product);
    }

    @DeleteMapping(DELETE_PRODUCT)
    public AskDTO deleteProduct(
            @PathVariable Long id
    ) {
        ProductEntity product = productHelper.getProductOrThrowException(id);

        productRepository.delete(product);

        return AskDTO.builder().answer(true).build();
    }

    private boolean checkWhetherProductBelongsToCategory(ProductEntity product, CategoryEntity category) {
        return category.getProducts()
                .stream()
                .anyMatch(p -> Objects.equals(p.getId(), product.getId()));
    }
}
