package com.powercoffee.powercoffeeapirest.service;

import com.powercoffee.powercoffeeapirest.payload.request.suppliers.SupplierRequest;
import com.powercoffee.powercoffeeapirest.payload.response.suppliers.SupplierResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;

import java.util.List;

public interface SupplierService {
    PaginationResponse<SupplierResponse> getAllSuppliers(String coffeeShopId, Integer pageNumber, Integer pageSize);
    List<SupplierResponse> getSuggestedSuppliers(String coffeeShopId);
    SupplierResponse getSupplierById(String supplierId, String coffeeShopId);
    SupplierResponse createSupplier(SupplierRequest supplierRequest, String coffeeShopId);
    SupplierResponse updateSupplier(SupplierRequest supplierRequest, String supplierId, String coffeeShopId);
    void deleteManySuppliers(String coffeeShopId, String[] ids);
    void deleteSupplier(String id, String coffeeShopId);

}
