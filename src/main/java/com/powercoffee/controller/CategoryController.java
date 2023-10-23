package com.powercoffee.controller;

import com.powercoffee.payload.request.category.CategoryRequest;
import com.powercoffee.payload.response.PaginationResponse;
import com.powercoffee.payload.response.category.CategoryResponse;
import com.powercoffee.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coffee-shops/{coffeeShopId}/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(
            @PathVariable String coffeeShopId) {
        return ResponseEntity.ok(categoryService.getAllCategories(coffeeShopId));
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

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable String coffeeShopId, @PathVariable String categoryId) {
        categoryService.deleteCategory(coffeeShopId, categoryId);
        return ResponseEntity.ok().build();
    }

}
