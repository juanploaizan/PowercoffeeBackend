package com.powercoffee.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.powercoffee.model.enums.ECity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@Table(name = "coffee_shops")
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeShop {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, length = 200)
    private String address;

    @Enumerated(EnumType.STRING)
    private ECity city;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    @JsonManagedReference
    private User admin;

    @OneToMany(mappedBy = "coffeeShop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Category> categories;
}
