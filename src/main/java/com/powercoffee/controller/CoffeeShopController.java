package com.powercoffee.controller;

import com.powercoffee.dto.CoffeeShopDTO;
import com.powercoffee.dto.PaginationResponse;
import com.powercoffee.service.CoffeeShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/coffee-shops")
@RequiredArgsConstructor
public class CoffeeShopController {

    private final CoffeeShopService coffeeShopService;

    @GetMapping
    public ResponseEntity<PaginationResponse<CoffeeShopDTO>> getAllCoffeeShops(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                                @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        return new ResponseEntity<>(coffeeShopService.getAllCoffeeShops(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoffeeShopDTO> getCoffeeShopById(@PathVariable Integer id) {
        return new ResponseEntity<>(coffeeShopService.getCoffeeShopById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CoffeeShopDTO> createCoffeeShop(@RequestBody CoffeeShopDTO coffeeShopDTO) {
        return new ResponseEntity<>(coffeeShopService.createCoffeeShop(coffeeShopDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoffeeShopDTO> updateCoffeeShop(@PathVariable Integer id, @RequestBody CoffeeShopDTO coffeeShopDTO) {
        return new ResponseEntity<>(coffeeShopService.updateCoffeeShop(id, coffeeShopDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoffeeShop(@PathVariable Integer id) {
        coffeeShopService.deleteCoffeeShop(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
