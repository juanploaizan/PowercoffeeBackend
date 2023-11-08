package com.powercoffee.powercoffeeapirest.repository;

import com.powercoffee.powercoffeeapirest.model.OrderDetail;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    Set<OrderDetail> findAllByOrderId(Integer order_id);

    @Query("SELECT od.product, SUM(od.quantity) as totalSold " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE o.date BETWEEN :startDate AND :endDate " +
            "AND o.status = com.powercoffee.powercoffeeapirest.model.enums.EOrderStatus.DELIVERED " +
            "AND o.coffeeShop.id = :coffeeShopId " +
            "GROUP BY od.product " +
            "ORDER BY totalSold DESC")
    List<Tuple> findTop5ProductsByTotalSoldBetweenDates(@Param("startDate") Date startDate,
                                                        @Param("endDate") Date endDate,
                                                        @Param("coffeeShopId") String coffeeShopId);

    void deleteAllByOrderId(Integer order_id);

}
