package com.powercoffee.service;

import com.powercoffee.model.CoffeeShop;

import java.util.List;

public interface CoffeeShopService {
    List<CoffeeShop> getAllCoffeeShops();

    CoffeeShop getCoffeeShop(Integer id);

    CoffeeShop saveCoffeeShop(CoffeeShop coffeeShop);

    void deleteCoffeeShop(Integer id);
}
