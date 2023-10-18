package com.powercoffee.service;


import com.powercoffee.payload.request.coffee_shops.CoffeeShopRequest;
import com.powercoffee.payload.response.PaginationResponse;
import com.powercoffee.payload.response.coffee_shops.CoffeeShopResponse;

import java.util.List;

public interface CoffeeShopService {
    CoffeeShopResponse createCoffeeShop(CoffeeShopRequest coffeeShopDTO);

    List<CoffeeShopResponse> getAllCoffeeShops(Integer adminId);

    CoffeeShopResponse getCoffeeShopById(Integer id);

    CoffeeShopResponse updateCoffeeShop(Integer id, CoffeeShopRequest coffeeShopDTO);

    void deleteCoffeeShop(Integer id);

    CoffeeShopResponse getFirstCoffeeShopByAdminId(Integer adminId);
}
