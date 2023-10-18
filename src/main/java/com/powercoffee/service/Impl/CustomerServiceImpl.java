package com.powercoffee.service.Impl;

import com.powercoffee.model.CoffeeShop;
import com.powercoffee.model.Customer;
import com.powercoffee.model.Gender;
import com.powercoffee.payload.request.customers.CreateCustomerRequest;
import com.powercoffee.payload.request.customers.CustomerRequest;
import com.powercoffee.payload.request.customers.UpdateCustomerRequest;
import com.powercoffee.payload.response.PaginationResponse;
import com.powercoffee.payload.response.customers.CustomerResponse;
import com.powercoffee.repository.CoffeeShopRepository;
import com.powercoffee.repository.CustomerRepository;
import com.powercoffee.repository.GenderRepository;
import com.powercoffee.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final GenderRepository genderRepository;

    private final CoffeeShopRepository coffeeShopRepository;

    private final ModelMapper modelMapper;


    public CustomerServiceImpl(CustomerRepository customerRepository, GenderRepository genderRepository, CoffeeShopRepository coffeeShopRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.genderRepository = genderRepository;
        this.coffeeShopRepository = coffeeShopRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomerResponse getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found by id " + id));
    }

    @Override
    public PaginationResponse<CustomerResponse> getAllCustomers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Customer> customerPage = customerRepository.findAll(pageable);

        List<CustomerResponse> customerResponseList = customerPage.getContent().stream().map(this::convertToResponse).toList();

        return new PaginationResponse<CustomerResponse>().build(
                customerResponseList,
                customerPage.getNumber(),
                customerPage.getSize(),
                customerPage.getNumberOfElements(),
                customerPage.getTotalPages(),
                sortBy,
                sortDir,
                customerPage.isFirst(),
                customerPage.isLast()
        );
    }

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest customerRequest) {

        Customer customer = Customer.builder()
                .phoneNumber(customerRequest.getPhoneNumber())
                .email(customerRequest.getEmail())
                .firstName(customerRequest.getFirstName())
                .lastName(customerRequest.getLastName())
                .birthdate(customerRequest.getBirthdate())
                .build();

        setCustomerGender(customer, customerRequest);
        setCustomerCoffeeShop(customer, customerRequest);

        return convertToResponse(customerRepository.save(customer));
    }

    @Override
    public CustomerResponse updateCustomer(Integer id, UpdateCustomerRequest customerRequest){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found by id " + id));

        customer.setPhoneNumber(customerRequest.getPhoneNumber() != null ? customerRequest.getPhoneNumber() : customer.getPhoneNumber());
        customer.setEmail(customerRequest.getEmail() != null ? customerRequest.getEmail() : customer.getEmail());
        customer.setFirstName(customerRequest.getFirstName() != null ? customerRequest.getFirstName() : customer.getFirstName());
        customer.setLastName(customerRequest.getLastName() != null ? customerRequest.getLastName() : customer.getLastName());
        customer.setBirthdate(customerRequest.getBirthdate() != null ? customerRequest.getBirthdate() : customer.getBirthdate());

        setCustomerGender(customer, customerRequest);
        setCustomerCoffeeShop(customer, customerRequest);

        return convertToResponse(customerRepository.save(customer));
    }

    private void setCustomerGender(Customer customer, CustomerRequest customerRequest) {
        boolean finded = false;
        if(customerRequest.getGender() != null){
            for (Gender gender : genderRepository.findAll()) {
                if(gender.getName().name()
                        .equalsIgnoreCase(customerRequest.getGender())){
                    customer.setGender(gender);
                    finded = true;
                    break;
                }
            }
            if (!finded){
                throw new EntityNotFoundException("Doesnt exists any Gender with name " + customerRequest.getGender());
            }
        }
    }

    private void setCustomerCoffeeShop(Customer customer, CustomerRequest customerRequest) {
        boolean finded = false;
        if(customerRequest.getCoffee_shop() != null){
            for (CoffeeShop coffeeShop : coffeeShopRepository.findAll()) {
                if (coffeeShop.getName().toLowerCase().replace(" ", "")
                        .equals(customerRequest.getCoffee_shop().toLowerCase().replace(" ",""))) {
                    customer.setCoffeeShop(coffeeShop);
                    finded = true;
                    break;
                }
            }
            if (!finded){
                throw new EntityNotFoundException("Doesnt exists any CoffeeShop with name " + customerRequest.getCoffee_shop());
            }
        }
    }

    @Override
    public void deleteCustomer(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found by id " + id));
        customerRepository.delete(customer);
    }

    private CustomerResponse convertToResponse(Customer customer) {
        return modelMapper.map(customer, CustomerResponse.class);
    }
}
