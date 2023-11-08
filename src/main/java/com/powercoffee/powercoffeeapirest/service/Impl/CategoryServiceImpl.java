package com.powercoffee.powercoffeeapirest.service.Impl;

import com.powercoffee.powercoffeeapirest.model.Category;
import com.powercoffee.powercoffeeapirest.model.CoffeeShop;
import com.powercoffee.powercoffeeapirest.payload.request.categories.CategoryRequest;
import com.powercoffee.powercoffeeapirest.payload.response.categories.CategoryResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;
import com.powercoffee.powercoffeeapirest.repository.CategoryRepository;
import com.powercoffee.powercoffeeapirest.repository.CoffeeShopRepository;
import com.powercoffee.powercoffeeapirest.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
    public PaginationResponse<CategoryResponse> getAllCategories(String coffeeShopId, Integer pageNumber, Integer pageSize) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Category> categoryPage = categoryRepository.findAllByCoffeeShopId(coffeeShopId, pageable);

        List<CategoryResponse> categoryResponses = categoryPage.map(this::convertToResponse).getContent();

        return new PaginationResponse<CategoryResponse>().build(
                categoryResponses,
                categoryPage.getNumber(),
                categoryPage.getSize(),
                categoryPage.getNumberOfElements(),
                categoryPage.getTotalPages(),
                categoryPage.isFirst(),
                categoryPage.isLast()
        );
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
    public void deleteAllCategories(String coffeeShopId, String[] categoryIds) {
        List<Category> categoryList = Arrays.stream(categoryIds)
                .map(id -> categoryRepository.findByIdAndCoffeeShopId(id, coffeeShopId)
                        .orElseThrow(() -> new EntityNotFoundException("Category not found by id " + id))
                )
                .toList();
        categoryRepository.deleteAll(categoryList);
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
        CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        categoryResponse.setCreatedAt(category.getCreatedAt() != null ? category.getCreatedAt().format(formatter) : null);
        return categoryResponse;
    }
}
