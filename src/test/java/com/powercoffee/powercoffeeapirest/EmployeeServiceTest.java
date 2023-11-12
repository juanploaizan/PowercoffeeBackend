package com.powercoffee.powercoffeeapirest;

import com.powercoffee.powercoffeeapirest.payload.request.employees.EmployeeRequest;
import com.powercoffee.powercoffeeapirest.payload.response.employees.EmployeeResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;
import com.powercoffee.powercoffeeapirest.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class EmployeeServiceTest {

    @Autowired
    EmployeeService employeeService;

    // Variables de entorno
    final String coffeeShopId = "9727f27a-2652-4abe-9aa1-e92209ac77d6";
    final String employeeId = "6a8632c1-ce21-4083-b6e5-70d79ff27f89";
    final String employee2Id = "98818c01-4348-4a73-a498-cc4d42ffb2e0";

    @Test
    @Rollback
    void createEmployeeTest() {

        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setDni("1237446115");
        employeeRequest.setEmail("tilinelmejor@uqvirtual.edu.co");
        employeeRequest.setPhoneNumber("3125321111");
        employeeRequest.setFirstName("Tilinaso Superior");
        employeeRequest.setLastName("Acosta Arracacha");
        employeeRequest.setBirthdate("2003-07-31");
        employeeRequest.setSalary(442002.24);
        employeeRequest.setAddress("Manzana verde Casa cuarta");
        employeeRequest.setHireDate("2015-11-25");
        employeeRequest.setGender("M");

        EmployeeResponse employeeResponse = employeeService.createEmployee(
                employeeRequest,
                coffeeShopId
        );

        Assertions.assertEquals("Tilinaso Superior", employeeResponse.getFirstName());
    }

    @Test
    @Rollback
    void getAllEmployeesTest() {
        PaginationResponse<EmployeeResponse> employeesResponse = employeeService.getAllEmployees(
                coffeeShopId,
                0,
                10
        );

        List<EmployeeResponse> employeesList = employeesResponse.getData();
        for (EmployeeResponse e: employeesList) {
            System.out.println("id:"+e.getId()+" | name:"+e.getFirstName());
        }

        Assertions.assertTrue(!employeesResponse.getData().isEmpty(), "La lista está vacia");
    }

    @Test
    @Rollback
    void getEmployeeByIdTest() {
        EmployeeResponse employeeResponse = employeeService.getEmployeeById(
                employeeId,
                coffeeShopId
        );
        System.out.println(employeeResponse.getFirstName());
        Assertions.assertNotNull(employeeResponse);
    }

    @Test
    @Rollback
    void updateEmployeeTest() {
        EmployeeResponse employee = employeeService.getEmployeeById(
                employeeId,
                coffeeShopId
        );

        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setDni(employee.getDni());
        employeeRequest.setEmail(employee.getEmail());
        employeeRequest.setPhoneNumber(employee.getPhoneNumber());
        employeeRequest.setFirstName("JOSe Antonio");
        employeeRequest.setLastName(employee.getLastName());
        employeeRequest.setBirthdate(employee.getBirthdate());
        employeeRequest.setSalary(employee.getSalary());
        employeeRequest.setAddress(employee.getAddress());
        employeeRequest.setHireDate(employee.getHireDate());
        employeeRequest.setGender(employee.getGender());

        EmployeeResponse employeeResponse = employeeService.updateEmployee(
                employeeRequest,
                employeeId,
                coffeeShopId
        );

        Assertions.assertEquals("JOSe Antonio", employeeResponse.getFirstName());
    }

    @Test
    @Rollback
    void deleteManyEmployeesTest() {
        String[] ids = {employeeId, employee2Id};
        EmployeeResponse employeeResponse = null;

        employeeService.deleteManyEmployees(
                coffeeShopId,
                ids
        );

        for(String id: ids) {
            try {
                employeeResponse = employeeService.getEmployeeById(
                        id,
                        coffeeShopId
                );
                // Si encuentra un employee eliminado, saltará al catch y continuará con la otra iteracion
                Assertions.assertNull(employeeResponse, "Empleado EXISTE todavia");
            } catch (EntityNotFoundException i) {
                continue; // Aunque se puede dejar vacio este bloque catch
            }
        }
        Assertions.assertNull(employeeResponse, "Empleado EXISTE todavia");
    }

    @Test
    @Rollback
    void deleteEmployeeTest() {
        employeeService.deleteEmployee(employeeId,coffeeShopId);
        EmployeeResponse employeeResponse = null;

        try {
            employeeResponse = employeeService.getEmployeeById(
                    employeeId,
                    coffeeShopId
            );
            // El mensaje de Assertions solo se muestra cuando falla el Assertions
            Assertions.assertNull(employeeResponse, "Empleado EXISTE todavia");
        } catch (EntityNotFoundException e) {
            Assertions.assertNull(employeeResponse, "Empleado EXISTE todavia");
        }

    }
}
