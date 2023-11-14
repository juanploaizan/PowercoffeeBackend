package com.powercoffee.powercoffeeapirest.repository;

import com.powercoffee.powercoffeeapirest.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String> {
    Page<Supplier> findAllByCoffeeShopIdAndDeletedAtIsNull(String coffeeShopId, Pageable pageable);
    @Query("SELECT s FROM Supplier s WHERE s.coffeeShop.id != :coffeeShopId AND s.deletedAt IS NULL")
    List<Supplier> findAllSuggestedByCoffeeShopIdAndDeletedAtIsNull(@Param("coffeeShopId") String coffeeShopId);
    Boolean existsByCoffeeShopIdAndNitAndDeletedAtIsNull(String coffeeShopId, String nit);
    Boolean existsByCoffeeShopIdAndEmailAndDeletedAtIsNull(String coffeeShopId, String email);
    Boolean existsByCoffeeShopIdAndPhoneNumberAndDeletedAtIsNull(String coffeeShopId, String phoneNumber);
    Optional<Supplier> findByIdAndDeletedAtIsNullAndCoffeeShopId(String id, String coffeeShopId);
}
