package com.powercoffee.controller;

import com.powercoffee.model.CoffeeShop;
import com.powercoffee.service.CoffeeShopService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CoffeeShopController {

    private final CoffeeShopService coffeeShopService;

    @GetMapping(value = "coffee-shops")
    public List<CoffeeShop> getAllCoffeeShops() {
        return coffeeShopService.getAllCoffeeShops();
    }

    @PostMapping(value = "coffee-shops")
    public String createCoffeeShop() {
        return "Welcome form secure endpoint coffee shop";
    }

}
