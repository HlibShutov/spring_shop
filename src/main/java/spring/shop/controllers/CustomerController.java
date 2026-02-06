package spring.shop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import spring.shop.exceptions.AccountNotFound;
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

    @GetMapping(value = "/customer", produces = "application/json")
    public Customer getCustomer(Authentication authentication) {
        return service.getCustomer(authentication.getName());
    }

    @PostMapping(value = "/customer", produces = "application/json")
    public Long createCustomer(Authentication authentication, @RequestBody Customer customer) {
        return service.createCustomer(authentication.getName(), customer);
    }

    @ExceptionHandler(CustomerNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleCustomerNotFound(CustomerNotFound e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Customer not found");
        errorResponse.put("message", e.getMessage());
        return errorResponse;
    }
    @ExceptionHandler(AccountNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleAccountNotFound(AccountNotFound e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Account not found");
        errorResponse.put("message", e.getMessage());
        return errorResponse;
    }
}
