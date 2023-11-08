package com.powercoffee.powercoffeeapirest.controller;

import com.powercoffee.powercoffeeapirest.payload.request.coffee_shops.CoffeeShopRequest;
import com.powercoffee.powercoffeeapirest.payload.response.coffee_shops.CoffeeShopResponse;
import com.powercoffee.powercoffeeapirest.payload.response.coffee_shops.MostSellProductResponse;
import com.powercoffee.powercoffeeapirest.payload.response.coffee_shops.RecentOrderResponse;
import com.powercoffee.powercoffeeapirest.service.CoffeeShopService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/coffee-shops")
@AllArgsConstructor
public class CoffeeShopController {

    private final CoffeeShopService coffeeShopService;

    @GetMapping
    public ResponseEntity<List<CoffeeShopResponse>> getAllCoffeeShops(@RequestParam(value = "adminId") Integer adminId) {
        return new ResponseEntity<>(coffeeShopService.getAllCoffeeShops(adminId), HttpStatus.OK);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<CoffeeShopResponse> getFirstCoffeeShopByAdminId(@PathVariable Integer id) {
        return new ResponseEntity<>(coffeeShopService.getFirstCoffeeShopByAdminId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoffeeShopResponse> getCoffeeShopById(@PathVariable String id) {
        return new ResponseEntity<>(coffeeShopService.getCoffeeShopById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/get-revenue")
    public ResponseEntity<Double> getRevenueByCoffeeShopId(
            @PathVariable String id,
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern="MM-dd-yyyy") Date fromDate,
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern="MM-dd-yyyy") Date toDate
    ) {
        return ResponseEntity.ok(coffeeShopService.getRevenueByCoffeeShopId(id, fromDate, toDate));
    }

    @GetMapping("/{id}/get-orders-count")
    public ResponseEntity<Integer> getOrdersCountByCoffeeShopId(
            @PathVariable String id,
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern="MM-dd-yyyy") Date fromDate,
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern="MM-dd-yyyy") Date toDate
    ) {
        return ResponseEntity.ok(coffeeShopService.getOrdersCountByCoffeeShopId(id, fromDate, toDate));
    }

    @GetMapping("/{id}/get-customers-count")
    public ResponseEntity<Integer> getCustomersCountByCoffeeShopId(
            @PathVariable String id,
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern="MM-dd-yyyy") Date fromDate,
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern="MM-dd-yyyy") Date toDate
    ) {
        return ResponseEntity.ok(coffeeShopService.getCustomersCountByCoffeeShopId(id, fromDate, toDate));
    }

    @GetMapping("/{id}/get-most-sell-products")
    public ResponseEntity<List<MostSellProductResponse>> getMostSellProductsByCoffeeShopId(
            @PathVariable String id,
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern="MM-dd-yyyy") Date fromDate,
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern="MM-dd-yyyy") Date toDate
    ) {
        return ResponseEntity.ok(coffeeShopService.getMostSellProductsByCoffeeShopId(id, fromDate, toDate));
    }

    @GetMapping("/{id}/get-most-recent-orders")
    public ResponseEntity<List<RecentOrderResponse>> getMostRecentOrdersByCoffeeShopId(@PathVariable String id) {
        return ResponseEntity.ok(coffeeShopService.getMostRecentOrders(id));
    }

    @PostMapping
    public ResponseEntity<CoffeeShopResponse> createCoffeeShop(@Valid @RequestBody CoffeeShopRequest coffeeShopDTO) {
        return new ResponseEntity<>(coffeeShopService.createCoffeeShop(coffeeShopDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoffeeShopResponse> updateCoffeeShop(@PathVariable String id, @Valid @RequestBody CoffeeShopRequest coffeeShopDTO) {
        return new ResponseEntity<>(coffeeShopService.updateCoffeeShop(id, coffeeShopDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCoffeeShop(@PathVariable String id) {
        coffeeShopService.deleteCoffeeShop(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
