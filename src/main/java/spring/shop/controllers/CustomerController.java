package spring.shop.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Customer", description = "Customer profile management")
@RestController
public class CustomerController {
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @Operation(summary = "Get current customer", description = "Returns the customer profile associated with the authenticated account.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Customer successfully retrieved"), @ApiResponse(responseCode = "404", description = "Customer not found") })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "/customer", produces = "application/json")
    public Customer getCustomer(Authentication authentication) {
        return service.getCustomer(authentication.getName());
    }

    @Operation(summary = "Create customer profile", description = "Creates a customer profile for the currently authenticated account.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Customer successfully created"), @ApiResponse(responseCode = "404", description = "Account not found") })
    @SecurityRequirement(name = "bearerAuth")
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
