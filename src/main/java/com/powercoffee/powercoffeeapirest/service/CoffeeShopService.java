package com.powercoffee.powercoffeeapirest.service;

import com.powercoffee.powercoffeeapirest.payload.request.coffee_shops.CoffeeShopRequest;
import com.powercoffee.powercoffeeapirest.payload.response.coffee_shops.CoffeeShopResponse;
import com.powercoffee.powercoffeeapirest.payload.response.coffee_shops.MostSellProductResponse;
import com.powercoffee.powercoffeeapirest.payload.response.coffee_shops.RecentOrderResponse;

import java.util.Date;
import java.util.List;

public interface CoffeeShopService {
    CoffeeShopResponse createCoffeeShop(CoffeeShopRequest coffeeShopDTO);

    List<CoffeeShopResponse> getAllCoffeeShops(Integer adminId);

    CoffeeShopResponse getCoffeeShopById(String id);

    CoffeeShopResponse updateCoffeeShop(String id, CoffeeShopRequest coffeeShopDTO);

    void deleteCoffeeShop(String id);

    CoffeeShopResponse getFirstCoffeeShopByAdminId(Integer adminId);

    Double getRevenueByCoffeeShopId(String id, Date fromDate, Date toDate);

    Integer getOrdersCountByCoffeeShopId(String id, Date fromDate, Date toDate);

    Integer getCustomersCountByCoffeeShopId(String id, Date fromDate, Date toDate);

    List<MostSellProductResponse> getMostSellProductsByCoffeeShopId(String id, Date fromDate, Date toDate);

    List<RecentOrderResponse> getMostRecentOrders(String id);
}
