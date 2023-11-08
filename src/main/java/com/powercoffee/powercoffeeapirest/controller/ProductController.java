package com.powercoffee.powercoffeeapirest.controller;

import com.powercoffee.powercoffeeapirest.payload.request.products.ProductRequest;
import com.powercoffee.powercoffeeapirest.payload.response.products.ProductResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;
import com.powercoffee.powercoffeeapirest.service.ProductService;
import com.powercoffee.powercoffeeapirest.utils.Constants;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coffee-shops/{coffeeShopId}/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Endpoint for retrieving all products of a coffee shop
    @GetMapping
    public ResponseEntity<PaginationResponse<ProductResponse>> getAllProducts(
            @PathVariable String coffeeShopId,
            @RequestParam(value = "pageNumber", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize
    ) {
        return ResponseEntity.ok(productService.getAllProducts(coffeeShopId, pageNumber, pageSize));
    }

    // Endpoint for retrieving a product by its ID
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable String coffeeShopId,
            @PathVariable String productId
    ) {
        return ResponseEntity.ok(productService.getProductById(coffeeShopId, productId));
    }

    // Endpoint for creating a new product
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @PathVariable String coffeeShopId,
            @Valid @RequestBody ProductRequest productRequest
    ) {
        return ResponseEntity.ok(productService.createProduct(coffeeShopId, productRequest));
    }

    // Endpoint for updating an existing product
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable String coffeeShopId,
            @PathVariable String productId,
            @Valid @RequestBody ProductRequest productRequest
    ) {
        return ResponseEntity.ok(productService.updateProduct(coffeeShopId, productId, productRequest));
    }

    // Endpoint for deleting a product
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable String coffeeShopId,
            @PathVariable String productId
    ) {
        productService.deleteProduct(coffeeShopId, productId);
        return ResponseEntity.ok().build();
    }

    // Endpoint for deleting multiple products
    @DeleteMapping
    public ResponseEntity<?> deleteManyProducts(
            @PathVariable String coffeeShopId,
            @RequestBody String[] productIds
    ) {
        productService.deleteManyProducts(coffeeShopId, productIds);
        return ResponseEntity.ok().build();
    }
}
