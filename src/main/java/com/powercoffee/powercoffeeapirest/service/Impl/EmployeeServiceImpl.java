package com.powercoffee.powercoffeeapirest.service.Impl;

import com.powercoffee.powercoffeeapirest.model.CoffeeShop;
import com.powercoffee.powercoffeeapirest.model.Employee;
import com.powercoffee.powercoffeeapirest.model.User;
import com.powercoffee.powercoffeeapirest.model.enums.EGender;
import com.powercoffee.powercoffeeapirest.payload.request.employees.EmployeeRequest;
import com.powercoffee.powercoffeeapirest.payload.response.customers.CustomerResponse;
import com.powercoffee.powercoffeeapirest.payload.response.employees.EmployeeResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;
import com.powercoffee.powercoffeeapirest.repository.CoffeeShopRepository;
import com.powercoffee.powercoffeeapirest.repository.EmployeeRepository;
import com.powercoffee.powercoffeeapirest.repository.UserRepository;
import com.powercoffee.powercoffeeapirest.security.services.UserDetailsImpl;
import com.powercoffee.powercoffeeapirest.service.EmployeeService;
import com.powercoffee.powercoffeeapirest.service.LoggerService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CoffeeShopRepository coffeeShopRepository;
    private final UserRepository userRepository;

    private final LoggerService loggerService;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, CoffeeShopRepository coffeeShopRepository, UserRepository userRepository, LoggerService loggerService, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.coffeeShopRepository = coffeeShopRepository;
        this.userRepository = userRepository;
        this.loggerService = loggerService;
        this.modelMapper = modelMapper;
    }

    @Override
    public PaginationResponse<EmployeeResponse> getAllEmployees(String coffeeShopId, Integer pageNumber, Integer pageSize) {
        Sort sort = Sort.by("dni").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Employee> employeePage = employeeRepository.findAllByCoffeeShopIdAndDeletedAtIsNull(coffeeShopId, pageable);
        List<EmployeeResponse> employeeResponseList = employeePage.getContent().stream().map(this::convertToResponse).toList();

        return new PaginationResponse<EmployeeResponse>().build(
                employeeResponseList,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getNumberOfElements(),
                employeePage.getTotalPages(),
                employeePage.isFirst(),
                employeePage.isLast()
        );
    }

    @Override
    public EmployeeResponse getEmployeeById(String employeeId, String coffeeShopId) {
        Employee employee = getEmployee(employeeId, coffeeShopId);
        EmployeeResponse employeeResponse = convertToResponse(employee);
        Integer userId = obtainIdFromJwtToken();
        loggerService.logAction("Employee", "READ", employeeResponse, employeeResponse, userId);
        return employeeResponse;
    }

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest, String coffeeShopId) {

        CoffeeShop coffeeShop = getCoffeeShop(coffeeShopId);

        if (employeeRepository.existsByCoffeeShopIdAndDni(coffeeShopId, employeeRequest.getDni())) {
            throw new EntityExistsException("Already exists an employee with DNI: "+ employeeRequest.getDni());
        }

        if (employeeRepository.existsByCoffeeShopIdAndPhoneNumber(coffeeShopId, employeeRequest.getPhoneNumber())) {
            throw new EntityExistsException("Already exists an employee with phone number: "+ employeeRequest.getPhoneNumber());
        }

        if (employeeRepository.existsByCoffeeShopIdAndEmail(coffeeShopId, employeeRequest.getEmail())) {
            throw new EntityExistsException("Already exists an employee with email: "+ employeeRequest.getEmail());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Employee employee = Employee.builder()
                .dni(employeeRequest.getDni())
                .email(employeeRequest.getEmail())
                .phoneNumber(employeeRequest.getPhoneNumber())
                .firstName(employeeRequest.getFirstName())
                .lastName(employeeRequest.getLastName())
                .birthdate(LocalDate.parse(employeeRequest.getBirthdate(), formatter))
                .salary(employeeRequest.getSalary())
                .address(employeeRequest.getAddress())
                .hireDate(LocalDate.parse(employeeRequest.getHireDate(), formatter))
                .gender(EGender.valueOf(employeeRequest.getGender()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .coffeeShop(coffeeShop)
                .build();

        EmployeeResponse employeeResponse = convertToResponse(employee);
        Integer userId = obtainIdFromJwtToken();
        loggerService.logAction("Employee", "CREATE", null, employeeResponse, userId);

        return convertToResponse(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(EmployeeRequest employeeRequest, String employeeId, String coffeeShopId) {

        Employee employee = getEmployee(employeeId, coffeeShopId);

        EmployeeResponse previousEmployeeResponse = convertToResponse(employee);

        if (!Objects.equals(employee.getDni(), employeeRequest.getDni())) {
            if(employeeRepository.existsByCoffeeShopIdAndDni(coffeeShopId, employeeRequest.getDni())) {
                throw new EntityNotFoundException("Already exists an employee with DNI: "+ employeeRequest.getDni());
            }
        }

        if (!Objects.equals(employee.getPhoneNumber(), employeeRequest.getPhoneNumber())) {
            if(employeeRepository.existsByCoffeeShopIdAndPhoneNumber(coffeeShopId, employeeRequest.getPhoneNumber())) {
                throw new EntityExistsException("Already exists an employee with phone number: "+ employeeRequest.getPhoneNumber());
            }
        }

        if (!Objects.equals(employee.getEmail(), employeeRequest.getEmail())) {
            if(employeeRepository.existsByCoffeeShopIdAndEmail(coffeeShopId, employeeRequest.getEmail())) {
                throw new EntityExistsException("Already exists an employee with email: "+ employeeRequest.getEmail());
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        employee.setDni(employeeRequest.getDni());
        employee.setEmail(employeeRequest.getEmail());
        employee.setPhoneNumber(employeeRequest.getPhoneNumber());
        employee.setFirstName(employeeRequest.getFirstName());
        employee.setLastName(employeeRequest.getLastName());
        employee.setBirthdate(LocalDate.parse(employeeRequest.getBirthdate(), formatter));
        employee.setSalary(employeeRequest.getSalary());
        employee.setAddress(employeeRequest.getAddress());
        employee.setHireDate(LocalDate.parse(employeeRequest.getHireDate(), formatter));
        employee.setGender(EGender.valueOf(employeeRequest.getGender()));
        employee.setUpdatedAt(LocalDateTime.now());

        EmployeeResponse actualEmployeeResponse = convertToResponse(employee);
        Integer userId = obtainIdFromJwtToken();
        loggerService.logAction("Employee", "UPDATE", previousEmployeeResponse, actualEmployeeResponse, userId);

        return convertToResponse(employeeRepository.save(employee));
    }
    @Override
    @Transactional
    public void deleteEmployee(String id, String coffeeShopId) {
        Employee employee = getEmployee(id, coffeeShopId);
        employee.setDeletedAt(LocalDateTime.now());
        EmployeeResponse previousEmployeeResponse = convertToResponse(employee);
        Integer userId = obtainIdFromJwtToken();
        loggerService.logAction("Employee", "DELETE", previousEmployeeResponse, null, userId);
        employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void deleteManyEmployees(String coffeeShopId, String[] ids) {
        for (String id : ids) {
            deleteEmployee(id, coffeeShopId);
        }
    }

    private Employee getEmployee(String employeeId, String coffeeShopId) {
        return employeeRepository.findByIdAndCoffeeShopIdAndDeletedAtIsNull(employeeId, coffeeShopId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found by id: "+employeeId));
    }

    private CoffeeShop getCoffeeShop(String coffeeShopId) {
        return coffeeShopRepository.findById(coffeeShopId)
                .orElseThrow(() -> new EntityNotFoundException("CoffeeShop not found by id: "+coffeeShopId));
    }

    private EmployeeResponse convertToResponse(Employee employee) {
        return modelMapper.map(employee, EmployeeResponse.class);
    }

    private Integer obtainIdFromJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("Error: User is not found."));
            return user.getId();
        }

        return null;
    }
}
