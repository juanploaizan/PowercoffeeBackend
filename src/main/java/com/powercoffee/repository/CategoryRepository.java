package com.powercoffee.repository;

import com.powercoffee.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    List<Category> findAllByCoffeeShopId(String coffeeShopId);
}
