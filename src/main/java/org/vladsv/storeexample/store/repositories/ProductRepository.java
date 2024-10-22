package org.vladsv.storeexample.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vladsv.storeexample.store.entities.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
