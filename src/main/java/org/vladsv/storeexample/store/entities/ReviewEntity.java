package org.vladsv.storeexample.store.entities;

import jakarta.persistence.*;
import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@Table(name = "review")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String author;

    private String content;

}
