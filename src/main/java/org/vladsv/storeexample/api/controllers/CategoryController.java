package org.vladsv.storeexample.api.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.vladsv.storeexample.api.controllers.helper.CategoryHelper;
import org.vladsv.storeexample.api.dto.AcknowledgmentDTO;
import org.vladsv.storeexample.api.dto.CategoryDTO;
import org.vladsv.storeexample.api.exceptions.BadRequestException;
import org.vladsv.storeexample.api.mappers.CategoryMapper;
import org.vladsv.storeexample.kafka.KafkaProducer;
import org.vladsv.storeexample.store.entities.CategoryEntity;
import org.vladsv.storeexample.store.repositories.CategoryRepository;

@Log4j2
@RestController
@RequiredArgsConstructor
//TODO: Could use @Transactional for proper db work.
public class CategoryController {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final CategoryHelper categoryHelper;

    private final KafkaProducer kafkaProducer;

    public static final String CREATE_CATEGORY = "api/v1/categories";
    public static final String GET_CATEGORY = "api/v1/categories/{id}";
    public static final String UPDATE_CATEGORY = "api/v1/categories/{id}";
    public static final String DELETE_CATEGORY = "api/v1/categories/{id}";

    @PostMapping(CREATE_CATEGORY)
    public CategoryDTO createCategory(@RequestParam String name) {
        if(name.trim().isEmpty()) {
            throw new BadRequestException("Category name cannot be empty");
        }

        categoryRepository
                .findByName(name)
                .ifPresent(project ->
                {
                    throw new BadRequestException(String.format("Category with name: \"%s\" already exists", name));
                });

        CategoryEntity category = categoryRepository.saveAndFlush(CategoryEntity.builder()
                .name(name)
                .build());

        kafkaProducer.sendMessage(String.format("category with name %s created", name));

        return categoryMapper.map(category);
    }

    @GetMapping(GET_CATEGORY)
    public CategoryDTO getCategory(@PathVariable("id") Long id) {

        CategoryEntity category = categoryHelper.getCategoryOrThrowException(id);

        return categoryMapper.map(category);
    }

    @PutMapping(UPDATE_CATEGORY)
    public CategoryDTO updateCategory(
            @PathVariable("id") Long id,
            @RequestParam String name) { //TODO:is there a way to get whole body and get data from it so it dont look silly?
        if(name.trim().isEmpty()) {
            throw new BadRequestException("Category name cannot be empty");
        }

        CategoryEntity category = categoryHelper.getCategoryOrThrowException(id);

        category.setName(name);
        category = categoryRepository.saveAndFlush(category);

        return categoryMapper.map(category);
    }

    @DeleteMapping(DELETE_CATEGORY)
    public AcknowledgmentDTO deleteCategory(@PathVariable("id") Long id) {
        CategoryEntity category = categoryHelper.getCategoryOrThrowException(id);

        categoryRepository.delete(category);
        return AcknowledgmentDTO.builder().answer(true).build();
    }

}
