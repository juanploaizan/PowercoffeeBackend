package com.powercoffee.powercoffeeapirest.controller;

import com.powercoffee.powercoffeeapirest.payload.request.categories.CategoryRequest;
import com.powercoffee.powercoffeeapirest.payload.response.categories.CategoryResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;
import com.powercoffee.powercoffeeapirest.service.CategoryService;
import com.powercoffee.powercoffeeapirest.utils.Constants;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coffee-shops/{coffeeShopId}/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<CategoryResponse>> getAllCategories(
            @RequestParam(value = "pageNumber", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @PathVariable String coffeeShopId) {
        return ResponseEntity.ok(categoryService.getAllCategories(coffeeShopId, pageNumber, pageSize));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable String coffeeShopId, @PathVariable String categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(coffeeShopId, categoryId));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest, @PathVariable String coffeeShopId) {
        return ResponseEntity.ok(categoryService.createCategory(coffeeShopId, categoryRequest));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@Valid @RequestBody CategoryRequest categoryRequest, @PathVariable String coffeeShopId, @PathVariable String categoryId) {
        return ResponseEntity.ok(categoryService.updateCategory(coffeeShopId, categoryId, categoryRequest));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteManyCategories(@PathVariable String coffeeShopId, @RequestBody String[] categoryIds) {
        categoryService.deleteAllCategories(coffeeShopId, categoryIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable String coffeeShopId, @PathVariable String categoryId) {
        categoryService.deleteCategory(coffeeShopId, categoryId);
        return ResponseEntity.ok().build();
    }

}
