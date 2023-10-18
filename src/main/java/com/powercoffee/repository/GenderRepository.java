package com.powercoffee.repository;

import com.powercoffee.model.Gender;
import com.powercoffee.model.Role;
import com.powercoffee.model.enums.EGender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {
    Optional<Gender> findByName(EGender name);
}
