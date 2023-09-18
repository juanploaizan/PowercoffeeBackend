package com.powercoffee.service.Impl;

import com.powercoffee.model.CoffeeShop;
import com.powercoffee.repository.CoffeeShopRepo;
import com.powercoffee.service.CoffeeShopService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class CoffeeShopServiceImpl implements CoffeeShopService {

    private CoffeeShopRepo coffeeShopRepo;

    @Override
    public List<CoffeeShop> getAllCoffeeShops() {
        return coffeeShopRepo.findAll();
    }

    @Override
    public CoffeeShop getCoffeeShop(Integer id) {
        return coffeeShopRepo.getReferenceById(id);
    }

    @Override
    public CoffeeShop saveCoffeeShop(CoffeeShop coffeeShop) {
        return coffeeShopRepo.save(coffeeShop);
    }

    @Override
    public void deleteCoffeeShop(Integer id) {
        coffeeShopRepo.deleteById(id);
    }


}
