package org.vladsv.storeexample.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vladsv.storeexample.store.entities.CategoryEntity;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);
}
