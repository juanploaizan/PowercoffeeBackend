package com.powercoffee.powercoffeeapirest.service.Impl;

import com.powercoffee.powercoffeeapirest.model.CoffeeShop;
import com.powercoffee.powercoffeeapirest.model.Customer;
import com.powercoffee.powercoffeeapirest.model.User;
import com.powercoffee.powercoffeeapirest.model.enums.EGender;
import com.powercoffee.powercoffeeapirest.payload.request.customers.CustomerRequest;
import com.powercoffee.powercoffeeapirest.payload.response.customers.CustomerResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;
import com.powercoffee.powercoffeeapirest.repository.CoffeeShopRepository;
import com.powercoffee.powercoffeeapirest.repository.CustomerRepository;
import com.powercoffee.powercoffeeapirest.repository.UserRepository;
import com.powercoffee.powercoffeeapirest.security.services.UserDetailsImpl;
import com.powercoffee.powercoffeeapirest.service.CustomerService;
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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CoffeeShopRepository coffeeShopRepository;

    private final UserRepository userRepository;

    private final LoggerService loggerService;

    private final ModelMapper modelMapper;


    public CustomerServiceImpl(CustomerRepository customerRepository, CoffeeShopRepository coffeeShopRepository, UserRepository userRepository, LoggerService loggerService, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.coffeeShopRepository = coffeeShopRepository;
        this.loggerService = loggerService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PaginationResponse<CustomerResponse> getAllCustomers(String coffeeShopId, Integer pageNumber, Integer pageSize) {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Customer> customerPage = customerRepository.findAllByCoffeeShopIdAndIsActive(coffeeShopId, true, pageable);

        List<CustomerResponse> customerResponseList = customerPage.getContent().stream().map(this::convertToResponse).toList();

        return new PaginationResponse<CustomerResponse>().build(
                customerResponseList,
                customerPage.getNumber(),
                customerPage.getSize(),
                customerPage.getNumberOfElements(),
                customerPage.getTotalPages(),
                customerPage.isFirst(),
                customerPage.isLast()
        );
    }

    @Override
    public CustomerResponse getCustomerById(String customerId, String coffeeShopId) {

        Customer customer = customerRepository.findByIdAndCoffeeShopIdAndIsActive(customerId, coffeeShopId, true)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found by id " + customerId));

        CustomerResponse customerResponse = convertToResponse(customer);
        Integer userId = obtainIdFromJwtToken();
        loggerService.logAction("Customer", "READ", customerResponse, customerResponse, userId);

        return convertToResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse createCustomer(CustomerRequest customerRequest, String coffeeShopId) {

        CoffeeShop coffeeShop = coffeeShopRepository.findById(coffeeShopId)
                .orElseThrow(() -> new EntityNotFoundException("CoffeeShop not found by id " + coffeeShopId));

        Customer customer = customerRepository.findByDniAndCoffeeShopId(customerRequest.getDni(), coffeeShopId);

        if (customer != null) {
            if (customer.getIsActive()) {
                throw new EntityExistsException("Already exists a customer with dni " + customerRequest.getDni());
            }
            customer.setIsActive(true);
            customer.setCoffeeShop(coffeeShop);
            return convertToResponse(customerRepository.save(customer));
        }

        if (customerRepository.existsByCoffeeShopIdAndPhoneNumber(coffeeShopId, customerRequest.getPhoneNumber())) {
            throw new EntityExistsException("Already exists a customer with phone number " + customerRequest.getPhoneNumber());
        }

        if (customerRepository.existsByCoffeeShopIdAndEmail(coffeeShopId, customerRequest.getEmail())) {
            throw new EntityExistsException("Already exists a customer with email " + customerRequest.getEmail());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        customer = Customer.builder()
                .dni(customerRequest.getDni())
                .email(customerRequest.getEmail())
                .phoneNumber(customerRequest.getPhoneNumber())
                .firstName(customerRequest.getFirstName())
                .lastName(customerRequest.getLastName())
                .birthdate(LocalDate.parse(customerRequest.getBirthDate(), formatter))
                .gender(EGender.valueOf(customerRequest.getGender()))
                .isActive(true)
                .createdAt(new Date())
                .updatedAt(new Date())
                .coffeeShop(coffeeShop)
                .build();

        CustomerResponse customerResponse = convertToResponse(customer);
        Integer userId = obtainIdFromJwtToken();
        loggerService.logAction("Customer", "CREATE", null, customerResponse, userId);

        return convertToResponse(customerRepository.save(customer));
    }

    @Override
    public CustomerResponse updateCustomer(CustomerRequest customerRequest, String customerId, String coffeeShopId) {

        Customer customer = customerRepository.findByIdAndCoffeeShopIdAndIsActive(customerId, coffeeShopId, true)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found by id " + customerId));

        CustomerResponse previousCustomerResponse = convertToResponse(customer);

        if (!Objects.equals(customer.getDni(), customerRequest.getDni())) {
            if (customerRepository.findByDniAndCoffeeShopIdAndIsActive(customerRequest.getDni(), coffeeShopId, true) != null) {
                throw new EntityExistsException("Already exists a customer with dni " + customerRequest.getDni());
            }
        }

        if (!Objects.equals(customer.getPhoneNumber(), customerRequest.getPhoneNumber())) {
            if (customerRepository.existsByCoffeeShopIdAndPhoneNumber(coffeeShopId, customerRequest.getPhoneNumber())) {
                throw new EntityExistsException("Already exists a customer with phone number " + customerRequest.getPhoneNumber());
            }
        }

        if (!Objects.equals(customer.getEmail(), customerRequest.getEmail())) {
            if (customerRepository.existsByCoffeeShopIdAndEmail(coffeeShopId, customerRequest.getEmail())) {
                throw new EntityExistsException("Already exists a customer with email " + customerRequest.getEmail());
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        customer.setDni(customerRequest.getDni());
        customer.setEmail(customerRequest.getEmail());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setBirthdate(LocalDate.parse(customerRequest.getBirthDate(), formatter));
        customer.setGender(EGender.valueOf(customerRequest.getGender()));
        customer.setUpdatedAt(new Date());

        CustomerResponse actualCustomerResponse = convertToResponse(customer);
        Integer userId = obtainIdFromJwtToken();
        loggerService.logAction("Customer", "UPDATE", previousCustomerResponse, actualCustomerResponse, userId);
        return convertToResponse(customerRepository.save(customer));
    }

    @Override
    public void deleteManyCustomers(String coffeeShopId, String[] ids) {
        List<Customer> customerList = Arrays.stream(ids)
                .map(id -> customerRepository.findByIdAndCoffeeShopIdAndIsActive(id, coffeeShopId, true)
                        .orElseThrow(() -> new EntityNotFoundException("Customer not found by id " + id))
                )
                .toList();
        customerList.forEach(customer -> {
            customer.setIsActive(false);
            customerRepository.save(customer);
        });
    }

    @Override
    public void deleteCustomer(String id, String coffeeShopId) {
        Customer customer = customerRepository.findByIdAndCoffeeShopIdAndIsActive(id, coffeeShopId, true)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found by id " + id));
        customer.setIsActive(false);
        CustomerResponse previousCustomerResponse = convertToResponse(customer);
        Integer userId = obtainIdFromJwtToken();
        loggerService.logAction("Customer", "DELETE", previousCustomerResponse, null, userId);
        customerRepository.save(customer);
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

    private CustomerResponse convertToResponse(Customer customer) {
        return modelMapper.map(customer, CustomerResponse.class);
    }
}
