package com.powercoffee.powercoffeeapirest.controller;

import com.powercoffee.powercoffeeapirest.payload.request.customers.CustomerRequest;
import com.powercoffee.powercoffeeapirest.payload.response.customers.CustomerResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.MessageResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;
import com.powercoffee.powercoffeeapirest.service.CustomerService;
import com.powercoffee.powercoffeeapirest.utils.Constants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coffee-shops/{coffeeShopId}/customers")
public class CustomerController {

    // Declare an instance of CustomerService to handle customer-related operations
    private final CustomerService customerService;

    // Constructor that initializes the CustomerService instance
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Endpoint for retrieving all customers with pagination support
    @GetMapping
    public ResponseEntity<PaginationResponse<CustomerResponse>> getAllCustomers(
            @RequestParam(value = "pageNumber", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @PathVariable String coffeeShopId) {
        return ResponseEntity.ok(customerService.getAllCustomers(coffeeShopId, pageNumber, pageSize));
    }

    // Endpoint for retrieving a specific customer by ID
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable String customerId, @PathVariable String coffeeShopId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId, coffeeShopId));
    }

    // Endpoint for creating a new customer
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest customerRequest, @PathVariable String coffeeShopId) {
        return new ResponseEntity<>(customerService.createCustomer(customerRequest, coffeeShopId), HttpStatus.CREATED);
    }

    // Endpoint for updating an existing customer by ID
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(@Valid @RequestBody CustomerRequest customerRequest, @PathVariable String customerId, @PathVariable String coffeeShopId) {
        return ResponseEntity.ok(customerService.updateCustomer(customerRequest, customerId, coffeeShopId));
    }

    // Endpoint for deleting multiple customers based on their IDs
    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteManyCustomers(@PathVariable String coffeeShopId, @RequestBody String[] ids) {
        customerService.deleteManyCustomers(coffeeShopId, ids);
        return ResponseEntity.ok().build();
    }

    // Endpoint for deleting a specific customer by ID
    @DeleteMapping("/{customerId}")
    public ResponseEntity<MessageResponse> deleteCustomer(@PathVariable String customerId, @PathVariable String coffeeShopId) {
        customerService.deleteCustomer(customerId, coffeeShopId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
