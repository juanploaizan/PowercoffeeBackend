package com.powercoffee;

import com.powercoffee.model.CoffeeShop;
import com.powercoffee.repository.CoffeeShopRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@Rollback()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CoffeeShopRepositoryTest {

    @Autowired
    private CoffeeShopRepository coffeeShopRepository;

    @Test
    void testAddCoffeeShop() {
        CoffeeShop coffeeShop = CoffeeShop.builder()
                .name("La esmeralda")
                .address("Barrio Granada Carrera 23d #8-18")
                .admin(null)
                .build();
        Assertions.assertNotNull(coffeeShopRepository.save(coffeeShop));
    }


}
