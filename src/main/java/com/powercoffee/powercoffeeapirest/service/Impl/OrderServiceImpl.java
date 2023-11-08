package com.powercoffee.powercoffeeapirest.service.Impl;

import com.powercoffee.powercoffeeapirest.model.*;
import com.powercoffee.powercoffeeapirest.model.enums.EOrderStatus;
import com.powercoffee.powercoffeeapirest.payload.request.orders.OrderDetailRequest;
import com.powercoffee.powercoffeeapirest.payload.request.orders.OrderRequest;
import com.powercoffee.powercoffeeapirest.payload.response.orders.OrderResponse;
import com.powercoffee.powercoffeeapirest.repository.*;
import com.powercoffee.powercoffeeapirest.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CoffeeShopRepository coffeeShopRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, CoffeeShopRepository coffeeShopRepository, CustomerRepository customerRepository, EmployeeRepository employeeRepository, ProductRepository productRepository, OrderDetailRepository orderDetailRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.coffeeShopRepository = coffeeShopRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<OrderResponse> getAllOrders(String coffeeShopId, Date startDate, Date endDate, String status) {
        // Change Date to Instant
        List<Order> orderList;
        if (startDate == null || endDate == null) {
            // 1. fechas nulas y estado nulo
            if (status == null) {
                orderList = orderRepository.findAllByCoffeeShopIdOrderByDateAsc(coffeeShopId);
            } else { // 2. fechas nulas y estado
                orderList = orderRepository.findAllByCoffeeShopIdAndStatusOrderByDateAsc(coffeeShopId, EOrderStatus.valueOf(status.toUpperCase()));
            }
        } else {
            Date newEndDate = Date.from(endDate.toInstant().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneId.systemDefault()).plusDays(1).minusSeconds(1).toInstant());
            if (status == null) { // 3. fechas y estado nulo
                orderList = orderRepository.findAllByCoffeeShopIdAndDateBetween(coffeeShopId, startDate, newEndDate);
            } else { // 4. fechas y estado
                orderList = orderRepository.findAllByCoffeeShopIdAndDateBetweenAndStatus(coffeeShopId, startDate, newEndDate, EOrderStatus.valueOf(status.toUpperCase()));
            }
        }
        return orderList.stream().map(this::convertToOrderResponse).toList();
    }

    @Override
    public OrderResponse getOrderById(String coffeeShopId, Integer orderId) {
        Order order = getOrder(orderId, coffeeShopId); // get order by id
        Set<OrderDetail> orderDetails =  orderDetailRepository.findAllByOrderId(orderId);
        order.setOrderDetails(orderDetails);
        return convertToOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse createOrder(String coffeeShopId, OrderRequest orderRequest) {

        CoffeeShop coffeeShop = getCoffeeShop(coffeeShopId); // validate and get coffee shop by id
        Customer customer = getCustomer(orderRequest.getCustomerId()); // validate and get customer by id
        Employee employee = getEmployee(orderRequest.getEmployeeId()); // validate and get employee by id

        HashMap<Product, Integer> products = validateProducts(orderRequest.getOrderDetails()); // Validate products before creating order

        Order order = new Order();
        order.setStatus(EOrderStatus.valueOf(orderRequest.getOrderStatus()));
        order.setCustomer(customer);
        order.setEmployee(employee);
        order.setCoffeeShop(coffeeShop);

        Set<OrderDetail> orderDetails = new HashSet<>();
        Double totalPrice = 0.0;
        for (Product product : products.keySet()) {
            int quantity = products.get(product);
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .quantity(quantity)
                    .productPrice(product.getSalePrice())
                    .subtotal(product.getSalePrice() * quantity)
                    .build();
            orderDetails.add(orderDetail);
            totalPrice += orderDetail.getSubtotal();
            product.setStock(product.getStock() - quantity);
            productRepository.save(product); // Save the new stock number
        }
        order.setOrderDetails(orderDetails);
        order.setTotalPrice(totalPrice);

        System.out.println(new Date());
        Order savedOrder = orderRepository.save(order);

        return convertToOrderResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(String coffeeShopId, Integer orderId, OrderRequest orderRequest) {

        Order order = getOrder(orderId, coffeeShopId); // get order by id
        Customer customer = getCustomer(orderRequest.getCustomerId()); // get customer by id
        Employee employee = getEmployee(orderRequest.getEmployeeId()); // get employee by id
        Set<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderId(orderId);

        // convert the array of order details to a set of order details
        Set<OrderDetail> orderDetailsRequest = new HashSet<>();
        for (OrderDetailRequest orderDetailRequest : orderRequest.getOrderDetails()) { // use enhanced for-loop
            Product product = getProduct(orderDetailRequest.getProductId()); // get product by id
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .quantity(orderDetailRequest.getQuantity())
                    .productPrice(product.getSalePrice())
                    .subtotal(product.getSalePrice() * orderDetailRequest.getQuantity())
                    .build();
            orderDetailsRequest.add(orderDetail);
        }

        if (!Objects.equals(orderDetails, orderDetailsRequest)) {
            HashMap<Product, Integer> products = validateProducts(orderRequest.getOrderDetails()); // Validate new products before updating order
            // Get all order details by order id and update stock
            for (OrderDetail orderDetail : orderDetails) {
                Product product = orderDetail.getProduct();
                product.setStock(product.getStock() + orderDetail.getQuantity());
                productRepository.save(product); // save product to database
            }
            orderDetailRepository.deleteAllByOrderId(orderId); // delete all order details by order id

            orderDetails = new HashSet<>();
            Double totalPrice = 0.0;
            for (Product product : products.keySet()) { // use enhanced for-loop
                int quantity = products.get(product);
                OrderDetail orderDetail = OrderDetail.builder()
                        .order(order)
                        .product(product)
                        .quantity(quantity)
                        .productPrice(product.getSalePrice())
                        .subtotal(product.getSalePrice() * quantity)
                        .build();
                orderDetails.add(orderDetail);
                totalPrice += orderDetail.getSubtotal();
                product.setStock(product.getStock() - quantity);
                productRepository.save(product); // save product to database
            }
            order.setTotalPrice(totalPrice);
            order.setOrderDetails(orderDetails);
        }

        order.setStatus(EOrderStatus.valueOf(orderRequest.getOrderStatus()));
        order.setCustomer(customer);
        order.setEmployee(employee);

        return convertToOrderResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse updateOrderStatus(String coffeeShopId, Integer orderId, String status) {
        Order order = getOrder(orderId, coffeeShopId); // get order by id
        order.setStatus(EOrderStatus.valueOf(status.toUpperCase()));
        return convertToOrderResponse(orderRepository.save(order));
    }

    private HashMap<Product, Integer> validateProducts(OrderDetailRequest[] orderDetails) {

        HashMap<Product, Integer> products = new HashMap<>();
        StringBuilder sb = new StringBuilder(); // use StringBuilder for string concatenation
        // Validar que el producto exista y que tenga stock
        for (OrderDetailRequest orderDetailRequest : orderDetails) { // use enhanced for-loop
            Product product = getProduct(orderDetailRequest.getProductId()); // get product by id
            if (orderDetailRequest.getQuantity() <= 0) {
                sb.append("Quantity must be greater than 0 for product ").append(product.getName()).append(". ");
                throw new PersistenceException(sb.toString());
            }
            if (product.getStock() < orderDetailRequest.getQuantity()) {
                sb.append("Product ").append(product.getName()).append(" is out of stock. There are ").append(product.getStock()).append(" units available."); // use StringBuilder
                throw new PersistenceException(sb.toString());
            } else {
                products.put(product, orderDetailRequest.getQuantity());
            }
        }
        return products;
    }

    private Product getProduct(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
    }

    private Employee getEmployee(String employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));
    }

    private Customer getCustomer(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + customerId));
    }

    private CoffeeShop getCoffeeShop(String coffeeShopId) {
        return coffeeShopRepository.findById(coffeeShopId)
                .orElseThrow(() -> new EntityNotFoundException("Coffee Shop not found with id: " + coffeeShopId));
    }

    private Order getOrder(Integer orderId, String coffeeShopId) {
        return orderRepository.findByIdAndCoffeeShopId(orderId, coffeeShopId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
    }

    private OrderResponse convertToOrderResponse(Order order) {
        // Convert Date to Human Readable Format MM-dd-yyyy HH:mm:ss
        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        orderResponse.setDate(formatter.format(order.getDate()));
        return orderResponse;
    }
}
