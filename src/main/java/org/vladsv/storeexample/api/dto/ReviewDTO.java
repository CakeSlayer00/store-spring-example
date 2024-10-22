package org.vladsv.storeexample.api.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReviewDTO {

    @NonNull
    private Long id;

    @NonNull
    private String title;

    @NonNull
    private String content;

    @NonNull
    private String author;
}
