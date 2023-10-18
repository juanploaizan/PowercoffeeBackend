package com.powercoffee.model;

import com.powercoffee.model.enums.EGender;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "genders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Gender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private EGender name;

    @OneToMany(mappedBy = "gender", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Customer> customers;
}
