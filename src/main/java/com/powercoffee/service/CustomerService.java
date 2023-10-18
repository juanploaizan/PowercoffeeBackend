package com.powercoffee.service;

import com.powercoffee.payload.request.customers.CreateCustomerRequest;
import com.powercoffee.payload.request.customers.UpdateCustomerRequest;
import com.powercoffee.payload.response.PaginationResponse;
import com.powercoffee.payload.response.customers.CustomerResponse;

public interface CustomerService {
    PaginationResponse<CustomerResponse> getAllCustomers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    CustomerResponse getCustomerById(Integer id);
    CustomerResponse createCustomer(CreateCustomerRequest customerRequest);
    CustomerResponse updateCustomer(Integer id, UpdateCustomerRequest customerRequest);
    void deleteCustomer(Integer id);
}
