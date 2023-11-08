package com.powercoffee.powercoffeeapirest.service.Impl;

import com.powercoffee.powercoffeeapirest.model.Category;
import com.powercoffee.powercoffeeapirest.model.CoffeeShop;
import com.powercoffee.powercoffeeapirest.model.Product;
import com.powercoffee.powercoffeeapirest.payload.request.products.ProductRequest;
import com.powercoffee.powercoffeeapirest.payload.response.products.ProductResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;
import com.powercoffee.powercoffeeapirest.repository.CategoryRepository;
import com.powercoffee.powercoffeeapirest.repository.CoffeeShopRepository;
import com.powercoffee.powercoffeeapirest.repository.ProductRepository;
import com.powercoffee.powercoffeeapirest.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final CoffeeShopRepository coffeeShopRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(CoffeeShopRepository coffeeShopRepository, CategoryRepository categoryRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.coffeeShopRepository = coffeeShopRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    // Implementation of the method to retrieve all products of a coffee shop
    @Override
    public PaginationResponse<ProductResponse> getAllProducts(String coffeeShopId, Integer pageNumber, Integer pageSize) {
        // Sorting and pagination configuration
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // Retrieve a page of products from the repository based on the coffee shop ID
        Page<Product> productsPage = productRepository.findAllByCoffeeShopIdAndDeletedAtIsNull(coffeeShopId, pageable);

        // Convert the products to a list of product responses
        List<ProductResponse> products = productsPage.getContent().stream()
                .map(this::convertToProductResponse)
                .toList();

        // Return a pagination response containing the product list and other pagination details
        return new PaginationResponse<ProductResponse>().build(
                products,
                productsPage.getNumber(),
                productsPage.getSize(),
                productsPage.getNumberOfElements(),
                productsPage.getTotalPages(),
                productsPage.isFirst(),
                productsPage.isLast()
        );
    }

    // Implementation of the method to retrieve a product by ID
    @Override
    public ProductResponse getProductById(String coffeeShopId, String productId) {
        // Retrieve the product from the repository by ID and coffee shop ID
        Product product = getProductByIdAndCoffeeShopId(productId, coffeeShopId);
        // Convert the product to a product response
        return convertToProductResponse(product);
    }

    // Implementation of the method to create a new product
    @Override
    public ProductResponse createProduct(String coffeeShopId, ProductRequest productRequest) {
        // Retrieve the coffee shop and category based on their IDs
        CoffeeShop coffeeShop = getCoffeeShopById(coffeeShopId);
        Category category = getCategoryById(productRequest.getCategoryId());

        // Create a new product instance with the provided details
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setImageUrl(productRequest.getImageUrl());
        product.setPurchasePrice(productRequest.getPurchasePrice());
        product.setSalePrice(productRequest.getSalePrice());
        product.setStock(productRequest.getStock());
        product.setCoffeeShop(coffeeShop);
        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        // Save the new product in the repository and convert it to a product response
        return convertToProductResponse(productRepository.save(product));
    }

    // Implementation of the method to update an existing product
    @Override
    public ProductResponse updateProduct(String coffeeShopId, String productId, ProductRequest productRequest) {
        // Retrieve the existing product based on its ID and the coffee shop ID
        Product product = getProductByIdAndCoffeeShopId(productId, coffeeShopId);
        // Retrieve the category based on the provided category ID
        Category category = getCategoryById(productRequest.getCategoryId());

        // Update the product details based on the provided request
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setImageUrl(productRequest.getImageUrl());
        product.setPurchasePrice(productRequest.getPurchasePrice());
        product.setSalePrice(productRequest.getSalePrice());
        product.setStock(productRequest.getStock());
        product.setCategory(category);
        product.setUpdatedAt(LocalDateTime.now());

        // Save the updated product in the repository and convert it to a product response
        return convertToProductResponse(productRepository.save(product));
    }

    // Implementation of the method to delete a product
    @Override
    public void deleteProduct(String coffeeShopId, String productId) {
        // Retrieve the product based on its ID and the coffee shop ID
        Product product = getProductByIdAndCoffeeShopId(productId, coffeeShopId);
        // Set the 'deletedAt' timestamp for the product and save it in the repository
        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    // Implementation of the method to delete multiple products
    @Override
    public void deleteManyProducts(String coffeeShopId, String[] productIds) {
        // Iterate through the provided array of product IDs and delete each product
        for (String productId : productIds) {
            deleteProduct(coffeeShopId, productId);
        }
    }

    // Helper method to retrieve a product by its ID and the coffee shop ID
    private Product getProductByIdAndCoffeeShopId(String productId, String coffeeShopId) {
        return productRepository.findByIdAndCoffeeShopIdAndDeletedAtIsNull(productId, coffeeShopId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
    }

    // Helper method to retrieve a coffee shop by its ID
    private CoffeeShop getCoffeeShopById(String coffeeShopId) {
        return coffeeShopRepository.findById(coffeeShopId)
                .orElseThrow(() -> new EntityNotFoundException("Coffee shop not found with id: " + coffeeShopId));
    }

    // Helper method to retrieve a category by its ID
    private Category getCategoryById(String categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));
    }

    // Helper method to convert a Product entity to a ProductResponse object
    private ProductResponse convertToProductResponse(Product product) {
        return modelMapper.map(product, ProductResponse.class);
    }
}
