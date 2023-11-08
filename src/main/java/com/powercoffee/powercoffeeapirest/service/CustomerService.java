package com.powercoffee.powercoffeeapirest.service;

import com.powercoffee.powercoffeeapirest.payload.request.customers.CustomerRequest;
import com.powercoffee.powercoffeeapirest.payload.response.customers.CustomerResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;

public interface CustomerService {
    PaginationResponse<CustomerResponse> getAllCustomers(String coffeeShopId, Integer pageNumber, Integer pageSize);
    CustomerResponse getCustomerById(String customerId, String coffeeShopId);
    CustomerResponse createCustomer(CustomerRequest customerRequest, String coffeeShopId);
    CustomerResponse updateCustomer(CustomerRequest customerRequest, String customerId, String coffeeShopId);
    void deleteManyCustomers(String coffeeShopId, String[] ids);
    void deleteCustomer(String id, String coffeeShopId);

}
