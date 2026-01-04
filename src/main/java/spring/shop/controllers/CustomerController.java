package spring.shop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spring.shop.exceptions.CustomerNotFound;
import spring.shop.exceptions.ProductNotFound;
import spring.shop.models.Customer;
import spring.shop.services.CustomerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CustomerController {
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping(value = "/customer/{id}", produces = "application/json")
    public Customer getCustomer(@PathVariable Long id) {
        return service.getCustomer(id);
    }
    @ExceptionHandler(CustomerNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleCustomerNotFound(CustomerNotFound e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Customer not found");
        errorResponse.put("message", e.getMessage());
        return errorResponse;
    }
}
