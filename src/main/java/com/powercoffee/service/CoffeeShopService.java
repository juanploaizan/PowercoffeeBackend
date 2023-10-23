package com.powercoffee.service;

import com.powercoffee.payload.request.coffee_shops.CoffeeShopRequest;
import com.powercoffee.payload.response.PaginationResponse;
import com.powercoffee.payload.response.coffee_shops.CoffeeShopResponse;

import java.util.List;

public interface CoffeeShopService {
    CoffeeShopResponse createCoffeeShop(CoffeeShopRequest coffeeShopDTO);

    List<CoffeeShopResponse> getAllCoffeeShops(Integer adminId);

    CoffeeShopResponse getCoffeeShopById(String id);

    CoffeeShopResponse updateCoffeeShop(String id, CoffeeShopRequest coffeeShopDTO);

    void deleteCoffeeShop(String id);

    CoffeeShopResponse getFirstCoffeeShopByAdminId(Integer adminId);
}
