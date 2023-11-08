package com.powercoffee.powercoffeeapirest.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.powercoffee.powercoffeeapirest.model.enums.EOrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date date;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EOrderStatus status;

    private Date updatedAt;

    // Relations

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderDetail> orderDetails;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    private CoffeeShop coffeeShop;

    @PrePersist
    public void prePersist() {
        this.date = new Date();
        this.status = EOrderStatus.PENDING;
    }
}
