package com.powercoffee.powercoffeeapirest.controller;

import com.powercoffee.powercoffeeapirest.payload.request.suppliers.SupplierRequest;
import com.powercoffee.powercoffeeapirest.payload.response.suppliers.SupplierResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.MessageResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;
import com.powercoffee.powercoffeeapirest.service.SupplierService;
import com.powercoffee.powercoffeeapirest.utils.Constants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coffee-shops/{coffeeShopId}/suppliers")
public class SupplierController {

    // Declare an instance of SupplierService to handle supplier-related operations
    private final SupplierService supplierService;

    // Constructor that initializes the SupplierService instance
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // Endpoint for retrieving all suppliers with pagination support
    @GetMapping
    public ResponseEntity<PaginationResponse<SupplierResponse>> getAllSuppliers(
            @RequestParam(value = "pageNumber", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @PathVariable String coffeeShopId) {
        return ResponseEntity.ok(supplierService.getAllSuppliers(coffeeShopId, pageNumber, pageSize));
    }

    // Endpoint for retrieving all suggested suppliers with pagination support
    @GetMapping("/suggested")
    public ResponseEntity<PaginationResponse<SupplierResponse>> getSuggestedSuppliers(
            @RequestParam(value = "pageNumber", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @PathVariable String coffeeShopId) {
        return ResponseEntity.ok(supplierService.getSuggestedSuppliers(coffeeShopId, pageNumber, pageSize));
    }

    // Endpoint for retrieving a specific supplier by ID
    @GetMapping("/{supplierId}")
    public ResponseEntity<SupplierResponse> getSupplierById(@PathVariable String supplierId, @PathVariable String coffeeShopId) {
        return ResponseEntity.ok(supplierService.getSupplierById(supplierId, coffeeShopId));
    }

    // Endpoint for creating a new supplier
    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(@Valid @RequestBody SupplierRequest supplierRequest, @PathVariable String coffeeShopId) {
        return new ResponseEntity<>(supplierService.createSupplier(supplierRequest, coffeeShopId), HttpStatus.CREATED);
    }

    // Endpoint for updating an existing supplier by ID
    @PutMapping("/{supplierId}")
    public ResponseEntity<SupplierResponse> updateSupplier(@Valid @RequestBody SupplierRequest supplierRequest, @PathVariable String supplierId, @PathVariable String coffeeShopId) {
        return ResponseEntity.ok(supplierService.updateSupplier(supplierRequest, supplierId, coffeeShopId));
    }

    // Endpoint for deleting multiple suppliers based on their IDs
    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteManySuppliers(@PathVariable String coffeeShopId, @RequestBody String[] ids) {
        supplierService.deleteManySuppliers(coffeeShopId, ids);
        return ResponseEntity.ok().build();
    }

    // Endpoint for deleting a specific supplier by ID
    @DeleteMapping("/{supplierId}")
    public ResponseEntity<MessageResponse> deleteSupplier(@PathVariable String supplierId, @PathVariable String coffeeShopId) {
        supplierService.deleteSupplier(supplierId, coffeeShopId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
