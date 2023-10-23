package com.powercoffee.service;

import com.powercoffee.payload.request.category.CategoryRequest;
import com.powercoffee.payload.response.PaginationResponse;
import com.powercoffee.payload.response.category.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(String coffeeShopId, CategoryRequest categoryRequest);

    CategoryResponse getCategoryById(String coffeeShopId, String categoryId);

    List<CategoryResponse> getAllCategories(String coffeeShopId);

    CategoryResponse updateCategory(String coffeeShopId, String categoryId, CategoryRequest categoryRequest);

    void deleteCategory(String coffeeShopId, String categoryId);
}
