package com.powercoffee.repository;

import com.powercoffee.model.CoffeeShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoffeeShopRepository extends JpaRepository<CoffeeShop, String> {
    CoffeeShop findFirstByAdmin_Id(Integer adminId);

    List<CoffeeShop> findAllByAdmin_Id(Integer adminId);
}
