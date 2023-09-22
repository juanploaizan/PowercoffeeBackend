package com.powercoffee.service.Impl;

import com.powercoffee.dto.CoffeeShopDTO;
import com.powercoffee.dto.PaginationResponse;
import com.powercoffee.exception.ResourceNotFoundException;
import com.powercoffee.model.CoffeeShop;
import com.powercoffee.repository.CoffeeShopRepo;
import com.powercoffee.service.CoffeeShopService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CoffeeShopServiceImpl implements CoffeeShopService {

    private CoffeeShopRepo coffeeShopRepo;


    @Override
    public CoffeeShopDTO createCoffeeShop(CoffeeShopDTO coffeeShopDTO) {
        CoffeeShop coffeeShop = convertToEntity(coffeeShopDTO);
        CoffeeShop createdCoffeeShop = coffeeShopRepo.save(coffeeShop);
        return convertToDTO(createdCoffeeShop);
    }

    @Override
    public PaginationResponse<CoffeeShopDTO> getAllCoffeeShops(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<CoffeeShop> coffeeShopPage = coffeeShopRepo.findAll(pageable);

        //Cambio de Entity a DTO
        List<CoffeeShopDTO> coffeeShopDTOList = coffeeShopPage.getContent().stream().map(this::convertToDTO).toList();

        PaginationResponse<CoffeeShopDTO> paginationResponse = new PaginationResponse<>();
        paginationResponse.setData(coffeeShopDTOList);
        paginationResponse.setPageNumber(coffeeShopPage.getNumber());
        paginationResponse.setPageSize(coffeeShopPage.getSize());
        paginationResponse.setTotalElements(coffeeShopPage.getNumberOfElements());
        paginationResponse.setTotalPages(coffeeShopPage.getTotalPages());
        paginationResponse.setFirst(coffeeShopPage.isFirst());
        paginationResponse.setLast(coffeeShopPage.isLast());
        paginationResponse.setSortBy(sortBy);
        paginationResponse.setSortDir(sortDir.toUpperCase());

        return paginationResponse;
    }

    @Override
    public CoffeeShopDTO getCoffeeShopById(Integer id) {
        return coffeeShopRepo.findById(id).map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Cafetería", "id", id));
    }

    @Override
    public CoffeeShopDTO updateCoffeeShop(Integer id, CoffeeShopDTO coffeeShopDTO) {

        CoffeeShop coffeeShop = coffeeShopRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cafetería", "id", id));
        coffeeShop.setName(coffeeShopDTO.getName());
        coffeeShop.setAddress(coffeeShopDTO.getAddress());

        return convertToDTO(coffeeShopRepo.save(coffeeShop));
    }

    @Override
    public void deleteCoffeeShop(Integer id) {
        CoffeeShop coffeeShop = coffeeShopRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cafetería", "id", id));
        coffeeShopRepo.delete(coffeeShop);
    }

    private CoffeeShopDTO convertToDTO (CoffeeShop coffeeShop) {
        return CoffeeShopDTO.builder()
                .id(coffeeShop.getId())
                .name(coffeeShop.getName())
                .address(coffeeShop.getAddress())
                .build();
    }

    private CoffeeShop convertToEntity(CoffeeShopDTO coffeeShopDTO) {
        return CoffeeShop.builder()
                .name(coffeeShopDTO.getName())
                .address(coffeeShopDTO.getAddress())
                .build();
    }
}
