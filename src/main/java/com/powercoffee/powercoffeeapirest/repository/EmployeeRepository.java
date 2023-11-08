package com.powercoffee.powercoffeeapirest.repository;

import com.powercoffee.powercoffeeapirest.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Page<Employee> findAllByCoffeeShopIdAndDeletedAtIsNull(String coffeeShopId, Pageable pageable);
    Optional<Employee> findByIdAndCoffeeShopIdAndDeletedAtIsNull(String id, String coffeeShopId);
    Boolean existsByCoffeeShopIdAndDni(String coffeeShopId, String dni);
    Boolean existsByCoffeeShopIdAndPhoneNumber(String coffeeShopId, String phoneNumber);
    Boolean existsByCoffeeShopIdAndEmail(String coffeeShopId, String email);
}
