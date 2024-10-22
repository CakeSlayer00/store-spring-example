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
@Table(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany
    @Builder.Default
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private List<ProductEntity> products = new ArrayList<>();
}
