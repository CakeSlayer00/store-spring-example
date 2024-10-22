package org.vladsv.storeexample.api.dto;

import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryDTO {

    @NonNull
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private List<ProductDTO> products;
}
