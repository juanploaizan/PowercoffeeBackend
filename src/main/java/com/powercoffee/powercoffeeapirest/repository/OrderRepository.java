package com.powercoffee.powercoffeeapirest.repository;

import com.powercoffee.powercoffeeapirest.model.Order;
import com.powercoffee.powercoffeeapirest.model.enums.EOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByCoffeeShopIdOrderByDateAsc(String coffeeShop_id);
    List<Order> findAllByCoffeeShopIdAndStatusOrderByDateAsc(String coffeeShopId, EOrderStatus status);
    List<Order> findAllByCoffeeShopIdAndDateBetween(String coffeeShop_id, Date date, Date date2);
    List<Order> findAllByCoffeeShopIdAndDateBetweenAndStatus(String coffeeShop_id, Date date, Date date2, EOrderStatus status);
    List<Order> findAllByCoffeeShopIdAndStatusOrderByDateDesc(String coffeeShop_id, EOrderStatus status);
    Optional<Order> findByIdAndCoffeeShopId(Integer id, String coffeeShopId);
    Integer countAllByCoffeeShopIdAndDateBetweenAndStatus(String coffeeShop_id, Date date, Date date2, EOrderStatus status);

}
