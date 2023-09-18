package com.powercoffee.repository;

import com.powercoffee.model.CoffeeShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeShopRepo extends JpaRepository<CoffeeShop, Integer> {
}
