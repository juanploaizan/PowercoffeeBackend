package com.powercoffee.service;

import com.powercoffee.dto.CoffeeShopDTO;
import com.powercoffee.dto.PaginationResponse;

import java.util.List;

public interface CoffeeShopService {
    CoffeeShopDTO createCoffeeShop(CoffeeShopDTO coffeeShopDTO);

    PaginationResponse<CoffeeShopDTO> getAllCoffeeShops(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    CoffeeShopDTO getCoffeeShopById(Integer id);

    CoffeeShopDTO updateCoffeeShop(Integer id, CoffeeShopDTO coffeeShopDTO);

    void deleteCoffeeShop(Integer id);

}
