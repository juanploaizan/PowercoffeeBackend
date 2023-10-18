package com.powercoffee.controller;

import com.powercoffee.payload.request.coffee_shops.CoffeeShopRequest;
import com.powercoffee.payload.response.PaginationResponse;
import com.powercoffee.payload.response.coffee_shops.CoffeeShopResponse;
import com.powercoffee.service.CoffeeShopService;
import com.powercoffee.utils.Constants;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CoffeeShopResponse> getCoffeeShopById(@PathVariable Integer id) {
        return new ResponseEntity<>(coffeeShopService.getCoffeeShopById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CoffeeShopResponse> createCoffeeShop(@Valid @RequestBody CoffeeShopRequest coffeeShopDTO) {
        return new ResponseEntity<>(coffeeShopService.createCoffeeShop(coffeeShopDTO), HttpStatus.CREATED);
    }

}
