package org.vladsv.storeexample.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vladsv.storeexample.store.entities.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
}
