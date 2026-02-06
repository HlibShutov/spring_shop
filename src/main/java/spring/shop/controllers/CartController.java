package spring.shop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import spring.shop.exceptions.*;
import spring.shop.models.Cart;
import spring.shop.services.CartService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    public CartController(CartService service) {
        this.service = service;
    }

    private CartService service;

    @GetMapping(value = "/create", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createCart(Authentication authentication) {
        return service.createCart(authentication.getName());
    }

    @GetMapping(value = "/", produces = "application/json")
    public Cart getCart(Authentication authentication) {
        return service.getCart(authentication.getName());
    }

    @DeleteMapping(value = "/")
    public void deleteCart(Authentication authentication) {
        service.deleteCart(authentication.getName());
    }

    @PutMapping(value = "/product/{productId}")
    public void addProductToCart(Authentication authentication, @PathVariable Long productId, @RequestParam Integer quantity) {
        service.addProductToCart(authentication.getName(), productId, quantity);
    }

    @PatchMapping(value = "/product/{productId}")
    public void removeProductFromCart(@PathVariable Long productId, @RequestParam Integer quantity, Authentication authentication) {
        service.removeProductFromCart(authentication.getName(), productId, quantity);
    }

    @DeleteMapping(value = "/product/{productId}")
    public void deleteProductFromCart(Authentication authentication, @PathVariable Long productId) {
        service.deleteProductFromCart(authentication.getName(), productId);
    }

    @ExceptionHandler(CartNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleCustomerNotFound(CartNotFound e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Cart not found");
        errorResponse.put("message", e.getMessage());
        return errorResponse;
    }
    @ExceptionHandler(CartWithCustomerExists.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleCartWithCustomerExists(CartWithCustomerExists e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Cart with this customer exists");
        errorResponse.put("message", e.getMessage());
        return errorResponse;
    }
    @ExceptionHandler(CartItemNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleCartItemNotFound(CartItemNotFound e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Cart item not found");
        errorResponse.put("message", e.getMessage());
        return errorResponse;
    }
    @ExceptionHandler(InvalidAmount.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleInvalidAmount(InvalidAmount e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Invalid amound");
        errorResponse.put("message", e.getMessage());
        return errorResponse;
    }
}