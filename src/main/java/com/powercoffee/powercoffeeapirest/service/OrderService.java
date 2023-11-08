package com.powercoffee.powercoffeeapirest.service;

import com.powercoffee.powercoffeeapirest.payload.request.orders.OrderRequest;
import com.powercoffee.powercoffeeapirest.payload.response.orders.OrderResponse;

import java.util.Date;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(String coffeeShopId, OrderRequest orderRequest);
    List<OrderResponse> getAllOrders(String coffeeShopId, Date startDate, Date endDate, String status);
    OrderResponse getOrderById(String coffeeShopId, Integer orderId);

    OrderResponse updateOrder(String coffeeShopId, Integer orderId, OrderRequest orderRequest);

    OrderResponse updateOrderStatus(String coffeeShopId, Integer orderId, String status);
}
