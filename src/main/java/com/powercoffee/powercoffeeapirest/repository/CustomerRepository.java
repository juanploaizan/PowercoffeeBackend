package com.powercoffee.powercoffeeapirest.repository;

import com.powercoffee.powercoffeeapirest.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Page<Customer> findAllByCoffeeShopIdAndIsActive(String coffeeShopId, Boolean isActive, Pageable pageable);
    Optional<Customer> findByIdAndCoffeeShopIdAndIsActive(String id, String coffeeShop_id, Boolean isActive);
    Customer findByDniAndCoffeeShopIdAndIsActive(String dni, String coffeeShop_id, Boolean isActive);
    Customer findByDniAndCoffeeShopId(String dni, String coffeeShopId);
    boolean existsByCoffeeShopIdAndEmail(String coffeeShopId, String email);
    boolean existsByCoffeeShopIdAndPhoneNumber(String coffeeShopId, String phoneNumber);
    Integer countAllByCoffeeShopIdAndCreatedAtBetween(String coffeeShop_id, Date createdAt, Date createdAt2);
}
