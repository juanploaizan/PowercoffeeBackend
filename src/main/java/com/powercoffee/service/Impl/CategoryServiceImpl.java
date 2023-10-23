package com.powercoffee.service.Impl;

import com.powercoffee.model.Category;
import com.powercoffee.model.CoffeeShop;
import com.powercoffee.payload.request.category.CategoryRequest;
import com.powercoffee.payload.response.PaginationResponse;
import com.powercoffee.payload.response.category.CategoryResponse;
import com.powercoffee.repository.CategoryRepository;
import com.powercoffee.repository.CoffeeShopRepository;
import com.powercoffee.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CoffeeShopRepository coffeeShopRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CoffeeShopRepository coffeeShopRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.coffeeShopRepository = coffeeShopRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CategoryResponse> getAllCategories(String coffeeShopId) {
        List<Category> categoryPage = categoryRepository.findAllByCoffeeShopId(coffeeShopId);
        return categoryPage.stream().map(this::convertToResponse).toList();
    }

    @Override
    public CategoryResponse getCategoryById(String coffeeShopId, String categoryId) {
        CoffeeShop coffeeShop = coffeeShopRepository.findById(coffeeShopId)
                .orElseThrow(() -> new EntityNotFoundException("CoffeeShop not found with id " + coffeeShopId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id " + categoryId));

        if (category.getCoffeeShop().getId().equals(coffeeShop.getId())) {
            return convertToResponse(category);
        } else {
            throw new EntityNotFoundException("Category not found with id " + categoryId);
        }
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(String coffeeShopId, CategoryRequest categoryRequest) {
        CoffeeShop coffeeShop = coffeeShopRepository.findById(coffeeShopId)
                .orElseThrow(() -> new EntityNotFoundException("CoffeeShop not found with id " + coffeeShopId));

        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setCoffeeShop(coffeeShop);

        return convertToResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(String coffeeShopId, String categoryId, CategoryRequest categoryRequest) {

        CoffeeShop coffeeShop = coffeeShopRepository.findById(coffeeShopId)
                .orElseThrow(() -> new EntityNotFoundException("CoffeeShop not found with id " + coffeeShopId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id " + categoryId));

        if (category.getCoffeeShop().getId().equals(coffeeShop.getId())) {
            category.setName(categoryRequest.getName());
            return convertToResponse(categoryRepository.save(category));
        } else {
            throw new EntityNotFoundException("Category not found with id " + categoryId);
        }
    }

    @Override
    @Transactional
    public void deleteCategory(String coffeeShopId, String categoryId) {
        CoffeeShop coffeeShop = coffeeShopRepository.findById(coffeeShopId)
                .orElseThrow(() -> new EntityNotFoundException("CoffeeShop not found with id " + coffeeShopId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id " + categoryId));

        if (category.getCoffeeShop().getId().equals(coffeeShop.getId())) {
            categoryRepository.delete(category);
        } else {
            throw new EntityNotFoundException("Category not found with id " + categoryId);
        }
    }

    private CategoryResponse convertToResponse(Category category) {
        return modelMapper.map(category, CategoryResponse.class);
    }
}
