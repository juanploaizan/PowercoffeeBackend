package com.powercoffee.powercoffeeapirest.repository;

import com.powercoffee.powercoffeeapirest.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    Page<Category> findAllByCoffeeShopId(String coffeeShopId, Pageable pageable);
    Optional<Category> findByIdAndCoffeeShopId(String id, String coffeeShop_id);
}
