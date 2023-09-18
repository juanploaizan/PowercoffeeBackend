package com.powercoffee.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.powercoffee.model.CoffeeShop;
import com.powercoffee.service.CoffeeShopService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coffee-shops")
@AllArgsConstructor
public class CoffeeShopController {

    private CoffeeShopService coffeeShopService;

    @GetMapping
    public ResponseEntity<List<CoffeeShop>> getAllCoffeeShops() {

        List<CoffeeShop> coffeeShops = coffeeShopService.getAllCoffeeShops();
        if (coffeeShops.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        for (CoffeeShop coffeeShop : coffeeShops) {
            coffeeShop.add(linkTo(methodOn(CoffeeShopController.class).getCoffeeShop(coffeeShop.getId())).withSelfRel());
            coffeeShop.add(linkTo(methodOn(CoffeeShopController.class).getAllCoffeeShops()).withRel(IanaLinkRelations.COLLECTION));
        }
        CollectionModel<CoffeeShop> model = CollectionModel.of(coffeeShops);
        model.add(linkTo(methodOn(CoffeeShopController.class).getAllCoffeeShops()).withSelfRel());
        return new ResponseEntity<>(coffeeShops, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoffeeShop> getCoffeeShop(@PathVariable Integer id) {
        System.err.println(id);
        try {
            CoffeeShop coffeeShop = coffeeShopService.getCoffeeShop(id);
            coffeeShop.add(linkTo(methodOn(CoffeeShopController.class).getCoffeeShop(coffeeShop.getId())).withSelfRel());
            coffeeShop.add(linkTo(methodOn(CoffeeShopController.class).getAllCoffeeShops()).withRel(IanaLinkRelations.COLLECTION));
            return new ResponseEntity<>(coffeeShop, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CoffeeShop> saveCoffeeShop(@RequestBody CoffeeShop coffeeShop) {
        try {
            CoffeeShop savedCoffeeShop = coffeeShopService.saveCoffeeShop(coffeeShop);
            savedCoffeeShop.add(linkTo(methodOn(CoffeeShopController.class).getCoffeeShop(savedCoffeeShop.getId())).withSelfRel());
            savedCoffeeShop.add(linkTo(methodOn(CoffeeShopController.class).getAllCoffeeShops()).withRel(IanaLinkRelations.COLLECTION));
            return ResponseEntity.created(linkTo(methodOn(CoffeeShopController.class).getCoffeeShop(savedCoffeeShop.getId())).toUri()).body(savedCoffeeShop);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<CoffeeShop> updateCoffeeShop(@RequestBody CoffeeShop coffeeShop) {
        try {
            CoffeeShop updatedCoffeeShop = coffeeShopService.saveCoffeeShop(coffeeShop);
            updatedCoffeeShop.add(linkTo(methodOn(CoffeeShopController.class).getCoffeeShop(updatedCoffeeShop.getId())).withSelfRel());
            updatedCoffeeShop.add(linkTo(methodOn(CoffeeShopController.class).getAllCoffeeShops()).withRel(IanaLinkRelations.COLLECTION));
            return new ResponseEntity<>(updatedCoffeeShop, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoffeeShop(@PathVariable Integer id) {
        try {
            coffeeShopService.deleteCoffeeShop(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
