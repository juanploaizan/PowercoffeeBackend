package com.powercoffee.service.Impl;

import com.powercoffee.model.CoffeeShop;
import com.powercoffee.model.User;
import com.powercoffee.model.enums.ECity;
import com.powercoffee.payload.request.coffee_shops.CoffeeShopRequest;
import com.powercoffee.payload.response.coffee_shops.CoffeeShopResponse;
import com.powercoffee.repository.CoffeeShopRepository;
import com.powercoffee.repository.UserRepository;
import com.powercoffee.service.CoffeeShopService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoffeeShopServiceImpl implements CoffeeShopService {

    private final CoffeeShopRepository coffeeShopRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public CoffeeShopServiceImpl(CoffeeShopRepository coffeeShopRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.coffeeShopRepository = coffeeShopRepository;
        this.userRepository = userRepository;
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

        return convertToResponse(coffeeShop);
    }

    @Override
    public CoffeeShopResponse getCoffeeShopById(String id) {
        return coffeeShopRepository.findById(id)
                .map(this::convertToResponse).orElseThrow(() -> new EntityNotFoundException("CoffeeShop not found with id " + id));
    }

    @Override
    public CoffeeShopResponse updateCoffeeShop(String id, CoffeeShopRequest coffeeShopDTO) {

        User admin = userRepository.findById(coffeeShopDTO.getAdminId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + coffeeShopDTO.getAdminId()));

        CoffeeShop coffeeShop = coffeeShopRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CoffeeShop not found with id " + id));

        coffeeShop.setName(coffeeShopDTO.getName());
        coffeeShop.setAddress(coffeeShopDTO.getAddress());
        coffeeShop.setCity(ECity.valueOf(coffeeShopDTO.getCity()));
        coffeeShop.setAdmin(admin);
        coffeeShop = coffeeShopRepository.save(coffeeShop);

        return convertToResponse(coffeeShop);
    }

    @Override
    public void deleteCoffeeShop(String id) {
        CoffeeShop coffeeShop = coffeeShopRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CoffeeShop not found with id " + id));
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

    private CoffeeShopResponse convertToResponse(CoffeeShop coffeeShop) {
        return modelMapper.map(coffeeShop, CoffeeShopResponse.class);
    }
}
