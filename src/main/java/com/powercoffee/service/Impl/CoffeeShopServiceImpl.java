package com.powercoffee.service.Impl;

import com.powercoffee.model.CoffeeShop;
import com.powercoffee.repository.CoffeeShopRepo;
import com.powercoffee.service.CoffeeShopService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CoffeeShopServiceImpl implements CoffeeShopService {

    private CoffeeShopRepo coffeeShopRepo;

    @Override
    public List<CoffeeShop> getAllCoffeeShops() {
        return coffeeShopRepo.findAll();
    }
}
