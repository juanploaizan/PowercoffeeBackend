package com.powercoffee.powercoffeeapirest.service;

import com.powercoffee.powercoffeeapirest.payload.request.categories.CategoryRequest;
import com.powercoffee.powercoffeeapirest.payload.response.categories.CategoryResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;

public interface CategoryService {
    PaginationResponse<CategoryResponse> getAllCategories(String coffeeShopId, Integer pageNumber, Integer pageSize);
    CategoryResponse getCategoryById(String coffeeShopId, String categoryId);
    CategoryResponse createCategory(String coffeeShopId, CategoryRequest categoryRequest);
    CategoryResponse updateCategory(String coffeeShopId, String categoryId, CategoryRequest categoryRequest);
    void deleteAllCategories(String coffeeShopId, String[] categoryIds);
    void deleteCategory(String coffeeShopId, String categoryId);

}
