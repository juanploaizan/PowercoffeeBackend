package com.powercoffee.powercoffeeapirest.service.Impl;

import com.powercoffee.powercoffeeapirest.model.CoffeeShop;
import com.powercoffee.powercoffeeapirest.model.Supplier;
import com.powercoffee.powercoffeeapirest.payload.request.suppliers.SupplierRequest;
import com.powercoffee.powercoffeeapirest.payload.response.suppliers.SupplierResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;
import com.powercoffee.powercoffeeapirest.repository.CoffeeShopRepository;
import com.powercoffee.powercoffeeapirest.repository.SupplierRepository;
import com.powercoffee.powercoffeeapirest.service.SupplierService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    private final CoffeeShopRepository coffeeShopRepository;

    private final ModelMapper modelMapper;


    public SupplierServiceImpl(SupplierRepository supplierRepository, CoffeeShopRepository coffeeShopRepository, ModelMapper modelMapper) {
        this.supplierRepository = supplierRepository;
        this.coffeeShopRepository = coffeeShopRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PaginationResponse<SupplierResponse> getAllSuppliers(String coffeeShopId, Integer pageNumber, Integer pageSize) {
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Supplier> supplierPage = supplierRepository.findAllByCoffeeShopIdAndDeletedAtIsNull(coffeeShopId, pageable);
        List<SupplierResponse> supplierResponseList = supplierPage.getContent().stream().map(this::convertToResponse).toList();
        return new PaginationResponse<SupplierResponse>().build(
                supplierResponseList,
                supplierPage.getNumber(),
                supplierPage.getSize(),
                supplierPage.getNumberOfElements(),
                supplierPage.getTotalPages(),
                supplierPage.isFirst(),
                supplierPage.isLast()
        );
    }

    @Override
    public List<SupplierResponse> getSuggestedSuppliers(String coffeeShopId) {
        List<Supplier> suppliers = supplierRepository.findAllSuggestedByCoffeeShopIdAndDeletedAtIsNull(coffeeShopId);
        return suppliers.stream().map(this::convertToResponse).toList();
    }

    @Override
    public SupplierResponse getSupplierById(String supplierId, String coffeeShopId) {
        Supplier supplier = getSupplier(supplierId, coffeeShopId);
        return convertToResponse(supplier);
    }

    @Override
    @Transactional
    public SupplierResponse createSupplier(SupplierRequest supplierRequest, String coffeeShopId) {

        CoffeeShop coffeeShop = getCoffeeShop(coffeeShopId);

        if (supplierRepository.existsByCoffeeShopIdAndNitAndDeletedAtIsNull(coffeeShopId, supplierRequest.getNit())) {
            throw new EntityExistsException("Already exists a supplier with NIT " + supplierRequest.getNit());
        }

        if (supplierRepository.existsByCoffeeShopIdAndEmailAndDeletedAtIsNull(coffeeShopId, supplierRequest.getEmail())) {
            throw new EntityExistsException("Already exists a supplier with email " + supplierRequest.getEmail());
        }

        if (supplierRepository.existsByCoffeeShopIdAndPhoneNumberAndDeletedAtIsNull(coffeeShopId, supplierRequest.getPhoneNumber())) {
            throw new EntityExistsException("Already exists a supplier with phone number " + supplierRequest.getPhoneNumber());
        }

        Supplier supplier = Supplier.builder()
                .nit(supplierRequest.getNit())
                .email(supplierRequest.getEmail())
                .phoneNumber(supplierRequest.getPhoneNumber())
                .name(supplierRequest.getName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .coffeeShop(coffeeShop)
                .build();

        return convertToResponse(supplierRepository.save(supplier));
    }

    @Override
    @Transactional
    public SupplierResponse updateSupplier(SupplierRequest supplierRequest, String supplierId, String coffeeShopId) {

        Supplier supplier = getSupplier(supplierId, coffeeShopId);

        if (!Objects.equals(supplier.getNit(), supplierRequest.getNit())) {
            if (supplierRepository.existsByCoffeeShopIdAndNitAndDeletedAtIsNull(coffeeShopId, supplierRequest.getNit())) {
                throw new EntityExistsException("Already exists a supplier with nit " + supplierRequest.getNit());
            }

        }
        if (!Objects.equals(supplier.getPhoneNumber(), supplierRequest.getPhoneNumber())) {
            if (supplierRepository.existsByCoffeeShopIdAndPhoneNumberAndDeletedAtIsNull(coffeeShopId, supplierRequest.getPhoneNumber())) {
                throw new EntityExistsException("Already exists a supplier with phone number " + supplierRequest.getPhoneNumber());
            }
        }

        if (!Objects.equals(supplier.getEmail(), supplierRequest.getEmail())) {
            if (supplierRepository.existsByCoffeeShopIdAndEmailAndDeletedAtIsNull(coffeeShopId, supplierRequest.getEmail())) {
                throw new EntityExistsException("Already exists a supplier with email " + supplierRequest.getEmail());
            }
        }

        supplier.setNit(supplierRequest.getNit());
        supplier.setEmail(supplierRequest.getEmail());
        supplier.setPhoneNumber(supplierRequest.getPhoneNumber());
        supplier.setName(supplierRequest.getName());
        supplier.setUpdatedAt(LocalDateTime.now());

        return convertToResponse(supplierRepository.save(supplier));
    }

    @Override
    @Transactional
    public void deleteSupplier(String id, String coffeeShopId) {
        Supplier supplier = getSupplier(id, coffeeShopId);
        supplier.setDeletedAt(LocalDateTime.now());
        supplierRepository.save(supplier);
    }

    @Override
    @Transactional
    public void deleteManySuppliers(String coffeeShopId, String[] ids) {
        for (String id : ids) {
            deleteSupplier(id, coffeeShopId);
        }
    }

    private Supplier getSupplier(String supplierId, String coffeeShopId) {
        return supplierRepository.findByIdAndDeletedAtIsNullAndCoffeeShopId(supplierId, coffeeShopId)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found by id " + supplierId));
    }

    private CoffeeShop getCoffeeShop(String coffeeShopId) {
        return coffeeShopRepository.findById(coffeeShopId)
                .orElseThrow(() -> new EntityNotFoundException("CoffeeShop not found by id " + coffeeShopId));
    }

    private SupplierResponse convertToResponse(Supplier supplier) {
        return modelMapper.map(supplier, SupplierResponse.class);
    }
}
