package org.vladsv.storeexample.api.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDTO {

    @NonNull
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private Double price;

    @NonNull
    private List<ReviewDTO> reviews;
}
