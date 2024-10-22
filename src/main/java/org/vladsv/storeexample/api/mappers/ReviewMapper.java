package org.vladsv.storeexample.api.mappers;

import org.springframework.stereotype.Component;
import org.vladsv.storeexample.api.dto.ReviewDTO;
import org.vladsv.storeexample.store.entities.ReviewEntity;

@Component
public class ReviewMapper {
    public ReviewDTO map(ReviewEntity review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .title(review.getTitle())
                .author(review.getAuthor())
                .content(review.getContent())
                .build();
    }
}
