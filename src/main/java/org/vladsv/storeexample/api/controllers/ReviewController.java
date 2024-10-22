package org.vladsv.storeexample.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.vladsv.storeexample.api.controllers.helper.ProductHelper;
import org.vladsv.storeexample.api.dto.AskDTO;
import org.vladsv.storeexample.api.dto.ReviewDTO;
import org.vladsv.storeexample.api.exceptions.BadRequestException;
import org.vladsv.storeexample.api.exceptions.NotFoundException;
import org.vladsv.storeexample.api.mappers.ReviewMapper;
import org.vladsv.storeexample.store.entities.ProductEntity;
import org.vladsv.storeexample.store.entities.ReviewEntity;
import org.vladsv.storeexample.store.repositories.ProductRepository;
import org.vladsv.storeexample.store.repositories.ReviewRepository;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewRepository reviewRepository;

    private final ProductRepository productRepository;

    private final ReviewMapper reviewMapper;

    private final ProductHelper productHelper;

    public static final String CREATE_REVIEW = "api/v1/{product_id}/reviews";
    public static final String GET_REVIEW = "api/v1/reviews/{id}";
    public static final String GET_ALL_REVIEWS = "api/v1/products/{product_id}/reviews";
    public static final String UPDATE_REVIEW = "api/v1/{product_id}/reviews/{id}";
    public static final String DELETE_REVIEW = "api/v1/reviews/{id}";

    @PostMapping(CREATE_REVIEW)
    public ReviewDTO createReview(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String author,
            @PathVariable("product_id") Long productId
    ) {

        if (title.trim().isEmpty()) {
            throw new BadRequestException("Review title cannot be empty");
        }

        if (content.trim().isEmpty()) {
            throw new BadRequestException("Content cannot be empty");
        }

        if (author.trim().isEmpty()) {
            throw new BadRequestException("Author cannot be empty");
        }

        ProductEntity product = productHelper.getProductOrThrowException(productId);

        ReviewEntity review = ReviewEntity.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        product.getReviews().add(review);

        reviewRepository.saveAndFlush(review);
        productRepository.saveAndFlush(product);

        return reviewMapper.map(review);
    }

    @GetMapping(GET_REVIEW)
    public ReviewDTO getReview(@PathVariable Long id) {

        ReviewEntity review = getReviewOrThrowException(id);

        return reviewMapper.map(review);
    }

    @GetMapping(GET_ALL_REVIEWS)
    public List<ReviewDTO> getAllReviews(
            @PathVariable("product_id") Long productId
    ) {

        return productHelper.getProductOrThrowException(productId)
                .getReviews()
                .stream()
                .map(reviewMapper::map)
                .toList();
    }

    @PutMapping(UPDATE_REVIEW)
    public ReviewDTO updateProduct(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String author,
            @PathVariable Long id,
            @PathVariable("product_id") Long productId
    ) {

        if (title.trim().isEmpty()) {
            throw new BadRequestException("Review title cannot be empty");
        }

        if (content.trim().isEmpty()) {
            throw new BadRequestException("Content cannot be empty");
        }

        if (author.trim().isEmpty()) {
            throw new BadRequestException("Author cannot be empty");
        }

        ProductEntity product = productHelper.getProductOrThrowException(productId);
        ReviewEntity review = getReviewOrThrowException(id);

        review.setTitle(title);
        review.setContent(content);
        review.setAuthor(author);

        if (checkWhetherReviewBelongsToProduct(review, product)) {
            product.getReviews().add(review);
            productRepository.saveAndFlush(product);
        }

        reviewRepository.saveAndFlush(review);

        return reviewMapper.map(review);
    }

    @DeleteMapping(DELETE_REVIEW)
    public AskDTO deleteProduct(
            @PathVariable Long id
    ) {
        ReviewEntity review = getReviewOrThrowException(id);
        reviewRepository.delete(review);

        return AskDTO.builder().answer(true).build();
    }

    private ReviewEntity getReviewOrThrowException(Long id) {
        return reviewRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Review with id: \"%s\" doesn't exist.",
                                        id
                                )
                        )
                );
    }

    private boolean checkWhetherReviewBelongsToProduct(ReviewEntity review, ProductEntity product) {
        return product.getReviews()
                .stream()
                .noneMatch(p -> Objects.equals(p.getId(), review.getId()));
    }
}
