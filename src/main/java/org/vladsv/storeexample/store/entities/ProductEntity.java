package org.vladsv.storeexample.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private Double price;

    private String description;

    @OneToMany
    @Builder.Default
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private List<ReviewEntity> reviews = new ArrayList<>();
}
