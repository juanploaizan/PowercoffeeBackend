package com.powercoffee.powercoffeeapirest.service;

import com.powercoffee.powercoffeeapirest.payload.request.products.ProductRequest;
import com.powercoffee.powercoffeeapirest.payload.response.products.ProductResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;

public interface ProductService {
    PaginationResponse<ProductResponse> getAllProducts(String coffeeShopId, Integer pageNumber, Integer pageSize);

    ProductResponse createProduct(String coffeeShopId, ProductRequest productRequest);

    ProductResponse getProductById(String coffeeShopId, String productId);

    ProductResponse updateProduct(String coffeeShopId, String productId, ProductRequest productRequest);

    void deleteProduct(String coffeeShopId, String productId);

    void deleteManyProducts(String coffeeShopId, String[] productIds);
}
