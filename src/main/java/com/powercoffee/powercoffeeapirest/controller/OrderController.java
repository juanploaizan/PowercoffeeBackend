package com.powercoffee.powercoffeeapirest.controller;

import com.powercoffee.powercoffeeapirest.payload.request.orders.OrderRequest;
import com.powercoffee.powercoffeeapirest.payload.response.orders.OrderResponse;
import com.powercoffee.powercoffeeapirest.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/coffee-shops/{coffeeShopId}/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate,
            @RequestParam(value = "status", required = false) String status,
            @PathVariable String coffeeShopId) {
        return ResponseEntity.ok(orderService.getAllOrders(coffeeShopId, startDate, endDate, status));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String coffeeShopId, @PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.getOrderById(coffeeShopId, orderId));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@PathVariable String coffeeShopId, @Valid @RequestBody OrderRequest orderRequest) {
        return new ResponseEntity<>(orderService.createOrder(coffeeShopId, orderRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable String coffeeShopId, @PathVariable Integer orderId, @Valid @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.updateOrder(coffeeShopId, orderId, orderRequest));
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable String coffeeShopId, @PathVariable Integer orderId, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(coffeeShopId, orderId, status));
    }
}
