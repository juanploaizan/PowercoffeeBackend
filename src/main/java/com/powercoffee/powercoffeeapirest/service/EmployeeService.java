package com.powercoffee.powercoffeeapirest.service;

import com.powercoffee.powercoffeeapirest.payload.request.employees.EmployeeRequest;
import com.powercoffee.powercoffeeapirest.payload.response.employees.EmployeeResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;

public interface EmployeeService {
    PaginationResponse<EmployeeResponse> getAllEmployees(String coffeeShopId, Integer pageNumber, Integer pageSize);
    EmployeeResponse getEmployeeById(String employeeId, String coffeeShopId);
    EmployeeResponse createEmployee(EmployeeRequest employeeRequest, String coffeeShopId);
    EmployeeResponse updateEmployee(EmployeeRequest employeeRequest, String employeeId, String coffeeShopId);
    void deleteManyEmployees(String coffeeShopId, String[] ids);
    void deleteEmployee(String id, String coffeeShopId);
}
