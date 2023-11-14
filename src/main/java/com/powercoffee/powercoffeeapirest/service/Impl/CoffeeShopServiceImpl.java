package com.powercoffee.powercoffeeapirest.service.Impl;

import com.powercoffee.powercoffeeapirest.model.CoffeeShop;
import com.powercoffee.powercoffeeapirest.model.Order;
import com.powercoffee.powercoffeeapirest.model.Product;
import com.powercoffee.powercoffeeapirest.model.User;
import com.powercoffee.powercoffeeapirest.model.enums.ECity;
import com.powercoffee.powercoffeeapirest.model.enums.EOrderStatus;
import com.powercoffee.powercoffeeapirest.payload.request.coffee_shops.CoffeeShopRequest;
import com.powercoffee.powercoffeeapirest.payload.response.coffee_shops.CoffeeShopResponse;
import com.powercoffee.powercoffeeapirest.payload.response.coffee_shops.MostSellProductResponse;
import com.powercoffee.powercoffeeapirest.payload.response.coffee_shops.RecentOrderResponse;
import com.powercoffee.powercoffeeapirest.repository.*;
import com.powercoffee.powercoffeeapirest.security.jwt.JwtUtils;
import com.powercoffee.powercoffeeapirest.security.services.UserDetailsImpl;
import com.powercoffee.powercoffeeapirest.service.CoffeeShopService;
import com.powercoffee.powercoffeeapirest.service.LoggerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Tuple;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CoffeeShopServiceImpl implements CoffeeShopService {

    private final CoffeeShopRepository coffeeShopRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;

    private final LoggerService loggerService;

    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;

    public CoffeeShopServiceImpl(CoffeeShopRepository coffeeShopRepository, UserRepository userRepository, OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, CustomerRepository customerRepository, LoggerService loggerService, JwtUtils jwtUtils, ModelMapper modelMapper) {
        this.coffeeShopRepository = coffeeShopRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.customerRepository = customerRepository;
        this.loggerService = loggerService;
        this.jwtUtils = jwtUtils;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CoffeeShopResponse> getAllCoffeeShops(Integer adminId) {

        userRepository.findById(adminId).orElseThrow(() -> new EntityNotFoundException("User not found with id " + adminId));

        List<CoffeeShop> coffeeShopPage = coffeeShopRepository.findAllByAdmin_Id(adminId);
        return coffeeShopPage.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public CoffeeShopResponse createCoffeeShop(CoffeeShopRequest coffeeShopDTO) {
        System.out.println(coffeeShopDTO.toString());

        User admin = userRepository.findById(coffeeShopDTO.getAdminId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + coffeeShopDTO.getAdminId()));

        CoffeeShop coffeeShop = new CoffeeShop();
        coffeeShop.setName(coffeeShopDTO.getName());
        coffeeShop.setAddress(coffeeShopDTO.getAddress());
        coffeeShop.setCity(ECity.valueOf(coffeeShopDTO.getCity()));
        coffeeShop.setAdmin(admin);
        coffeeShop = coffeeShopRepository.save(coffeeShop);

        CoffeeShopResponse actualCoffeeShopResponse = convertToResponse(coffeeShop);
        Integer userId = obtainIdFromJwtToken();
        loggerService.logAction("CoffeeShop", "CREATE", null, actualCoffeeShopResponse, userId);

        return actualCoffeeShopResponse;
    }

    @Override
    public CoffeeShopResponse getCoffeeShopById(String id) {
        CoffeeShop coffeeShop = getCoffeeShop(id);
        CoffeeShopResponse actualCoffeeShopResponse = convertToResponse(coffeeShop);
        Integer userId = obtainIdFromJwtToken();
        loggerService.logAction("CoffeeShop", "READ", actualCoffeeShopResponse, actualCoffeeShopResponse, userId);
        return convertToResponse(coffeeShop);
    }

    @Override
    public CoffeeShopResponse updateCoffeeShop(String id, CoffeeShopRequest coffeeShopDTO) {

        User admin = userRepository.findById(coffeeShopDTO.getAdminId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + coffeeShopDTO.getAdminId()));

        CoffeeShop coffeeShop = getCoffeeShop(id);
        CoffeeShopResponse previousCoffeeShopResponse = convertToResponse(coffeeShop);
        coffeeShop.setName(coffeeShopDTO.getName());
        coffeeShop.setAddress(coffeeShopDTO.getAddress());
        coffeeShop.setCity(ECity.valueOf(coffeeShopDTO.getCity()));
        coffeeShop.setAdmin(admin);
        coffeeShop = coffeeShopRepository.save(coffeeShop);

        CoffeeShopResponse actualCoffeeShopResponse = convertToResponse(coffeeShop);
        Integer userId = obtainIdFromJwtToken();
        loggerService.logAction("CoffeeShop", "UPDATE", previousCoffeeShopResponse, actualCoffeeShopResponse, userId);

        return convertToResponse(coffeeShop);
    }

    @Override
    public void deleteCoffeeShop(String id) {
        CoffeeShop coffeeShop = getCoffeeShop(id);
        CoffeeShopResponse previousCoffeeShopResponse = convertToResponse(coffeeShop);
        Integer userId = obtainIdFromJwtToken();
        loggerService.logAction("CoffeeShop", "DELETE", previousCoffeeShopResponse, null, userId);
        coffeeShopRepository.delete(coffeeShop);
    }

    @Override
    public CoffeeShopResponse getFirstCoffeeShopByAdminId(Integer adminId) {
        userRepository.findById(adminId).orElseThrow(() -> new EntityNotFoundException("User not found with id " + adminId));
        CoffeeShop coffeeShop = coffeeShopRepository.findFirstByAdmin_Id(adminId);
        if (coffeeShop != null) {
            return convertToResponse(coffeeShop);
        }
        return null;
    }

    @Override
    public Double getRevenueByCoffeeShopId(String id, Date fromDate, Date toDate) {
        CoffeeShop coffeeShop = getCoffeeShop(id);
        toDate = getNewToDate(toDate);
        List<Order> orders = orderRepository.findAllByCoffeeShopIdAndDateBetweenAndStatus(coffeeShop.getId(), fromDate, toDate, EOrderStatus.DELIVERED);
        Double revenue = 0.0;
        for (Order order : orders) {
            revenue += order.getTotalPrice();
        }
        return revenue;
    }

    @Override
    public Integer getOrdersCountByCoffeeShopId(String id, Date fromDate, Date toDate) {
        CoffeeShop coffeeShop = getCoffeeShop(id);
        toDate = getNewToDate(toDate);
        return orderRepository.countAllByCoffeeShopIdAndDateBetweenAndStatus(coffeeShop.getId(), fromDate, toDate, EOrderStatus.DELIVERED);
    }

    @Override
    public Integer getCustomersCountByCoffeeShopId(String id, Date fromDate, Date toDate) {
        CoffeeShop coffeeShop = getCoffeeShop(id);
        toDate = getNewToDate(toDate);
        return customerRepository.countAllByCoffeeShopIdAndCreatedAtBetween(coffeeShop.getId(), fromDate, toDate);
    }

    @Override
    public List<MostSellProductResponse> getMostSellProductsByCoffeeShopId(String id, Date fromDate, Date toDate) {
        CoffeeShop coffeeShop = getCoffeeShop(id);
        toDate = getNewToDate(toDate);

        List<Tuple> tuples = orderDetailRepository.findTop5ProductsByTotalSoldBetweenDates(fromDate, toDate, coffeeShop.getId());
        List<MostSellProductResponse> products = new ArrayList<>();
        for (Tuple tuple : tuples) {
            Product product = (Product) tuple.get(0);
            Long totalSold = (Long) tuple.get(1);
            MostSellProductResponse mostSellProductResponse = new MostSellProductResponse(product.getName(), totalSold);
            products.add(mostSellProductResponse);
        }
        return products;
    }

    @Override
    public List<RecentOrderResponse> getMostRecentOrders(String id) {
        CoffeeShop coffeeShop = getCoffeeShop(id);
        List<Order> orders = orderRepository.findAllByCoffeeShopIdAndStatusOrderByDateDesc(coffeeShop.getId(), EOrderStatus.DELIVERED);
        List<RecentOrderResponse> recentOrderResponses = new ArrayList<>();
        for (int i= 0; i < orders.size() && i < 5; i++) {
            Order order = orders.get(i);
            RecentOrderResponse recentOrderResponse = getRecentOrderResponse(order);
            recentOrderResponses.add(recentOrderResponse);
        }
        return recentOrderResponses;
    }

    private static RecentOrderResponse getRecentOrderResponse(Order order) {
        RecentOrderResponse recentOrderResponse = new RecentOrderResponse();
        recentOrderResponse.setClientName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
        recentOrderResponse.setClientEmail(order.getCustomer().getEmail());
        recentOrderResponse.setInitials(order.getCustomer().getFirstName().substring(0, 1).toUpperCase() + order.getCustomer().getLastName().substring(0, 1).toUpperCase());
        recentOrderResponse.setTotal(order.getTotalPrice());
        return recentOrderResponse;
    }

    private CoffeeShop getCoffeeShop(String id) {
        return coffeeShopRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CoffeeShop not found with id " + id));
    }

    private Date getNewToDate(Date toDate) {
        return Date.from(toDate.toInstant().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneId.systemDefault()).plusDays(1).minusSeconds(1).toInstant());
    }

    private CoffeeShopResponse convertToResponse(CoffeeShop coffeeShop) {
        return modelMapper.map(coffeeShop, CoffeeShopResponse.class);
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
