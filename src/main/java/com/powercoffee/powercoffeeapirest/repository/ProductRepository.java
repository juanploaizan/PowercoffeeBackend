package com.powercoffee.powercoffeeapirest.repository;

import com.powercoffee.powercoffeeapirest.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findAllByCoffeeShopIdAndDeletedAtIsNull(String coffeeShopId, Pageable pageable);

    Optional<Product> findByIdAndCoffeeShopIdAndDeletedAtIsNull(String id, String coffeeShopId);

}
