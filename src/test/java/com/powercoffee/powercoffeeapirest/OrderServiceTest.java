package com.powercoffee.powercoffeeapirest;

import com.powercoffee.powercoffeeapirest.model.enums.EOrderStatus;
import com.powercoffee.powercoffeeapirest.payload.request.orders.OrderRequest;
import com.powercoffee.powercoffeeapirest.payload.response.orders.OrderResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;
import com.powercoffee.powercoffeeapirest.service.OrderService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    OrderService orderService;

    final String coffeeShopId = "9727f27a-2652-4abe-9aa1-e92209ac77d6";
    final String employeeId = "6a8632c1-ce21-4083-b6e5-70d79ff27f89";
    final String customerId = "ceac5828-7739-4352-88a2-da115afc8071";

    @Test
    @Rollback
    void createOrderTest() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setEmployeeId(employeeId);
        orderRequest.setCustomerId(customerId);


        OrderResponse orderResponse = orderService.createOrder(
                coffeeShopId,
                orderRequest
        );
    }

    @Test
    @Rollback
    void getAllOrders() {
        /*List<OrderResponse> ordersResponse = orderService.getAllOrders(
                coffeeShopId,
                Date.from(LocalDate.of(2023, 10, 30).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDate.of(2023, 12, 30).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                EOrderStatus.PENDING.toString()
        );*/

        List<OrderResponse> ordersResponse = orderService.getAllOrders(
                coffeeShopId,
                null,
                null,
                EOrderStatus.PENDING.toString()
        );

        /*for (OrderResponse o: ordersResponse) {
            System.out.println("id:"+o.getId());
        }*/

        //Assertions.assertTrue(!ordersResponse.isEmpty(), "La lista está vacia");
    }

    @Test
    @Rollback
    void getOrderById() {
        OrderResponse orderResponse = orderService.getOrderById(coffeeShopId,1);
        System.out.println("id:"+orderResponse.getId()+" | date:"+orderResponse.getDate());
        /*System.out.println("id:"+orderResponse.getId()+" | date:"+orderResponse.getDate());

        Assertions.assertNotNull(orderResponse, "Orden no existe");*/
    }

    @Test
    @Rollback
    void updateOrder() {

    }

    @Test
    @Rollback
    void updateOrderStatus() {

    }
}
