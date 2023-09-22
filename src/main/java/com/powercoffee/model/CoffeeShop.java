package com.powercoffee.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@Table(name = "coffee_shops")
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeShop extends RepresentationModel<CoffeeShop> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String address;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    private User admin;

}
