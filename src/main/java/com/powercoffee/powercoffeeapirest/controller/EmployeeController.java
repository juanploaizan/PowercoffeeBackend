package com.powercoffee.powercoffeeapirest.controller;

import com.powercoffee.powercoffeeapirest.payload.request.employees.EmployeeRequest;
import com.powercoffee.powercoffeeapirest.payload.response.employees.EmployeeResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.MessageResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;
import com.powercoffee.powercoffeeapirest.service.EmployeeService;
import com.powercoffee.powercoffeeapirest.utils.Constants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coffee-shops/{coffeeShopId}/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<EmployeeResponse>> getAllEmployees(
            @RequestParam(value = "pageNumber", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @PathVariable String coffeeShopId) {
        return ResponseEntity.ok(employeeService.getAllEmployees(coffeeShopId, pageNumber, pageSize));
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable String employeeId, @PathVariable String coffeeShopId) {
        return ResponseEntity.ok(employeeService.getEmployeeById(employeeId, coffeeShopId));
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest, @PathVariable String coffeeShopId) {
        return new ResponseEntity<>(employeeService.createEmployee(employeeRequest, coffeeShopId), HttpStatus.CREATED);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> updateEmployee(@Valid @RequestBody EmployeeRequest employeeRequest, @PathVariable String employeeId, @PathVariable String coffeeShopId) {
        return ResponseEntity.ok(employeeService.updateEmployee(employeeRequest, employeeId, coffeeShopId));
    }

    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteManyEmployees(@PathVariable String coffeeShopId, @RequestBody String[] ids) {
        employeeService.deleteManyEmployees(coffeeShopId, ids);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<MessageResponse> deleteEmployee(@PathVariable String employeeId, @PathVariable String coffeeShopId) {
        employeeService.deleteEmployee(employeeId, coffeeShopId);
        return ResponseEntity.ok().build();
    }
}