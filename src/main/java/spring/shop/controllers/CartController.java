package spring.shop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spring.shop.exceptions.*;
import spring.shop.models.Cart;
import spring.shop.services.CartService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CartController {
    public CartController(CartService service) {
        this.service = service;
    }

    private CartService service;

    @GetMapping(value = {"/create_cart/{id}", "/create_cart"}, produces = "application/json")
    public Long createCart(@PathVariable(required = false) Long id) {
        return service.createCart(id);
    }

    @GetMapping(value = "/get_cart/{id}", produces = "application/json")
    public Cart getCart(@PathVariable Long id) {
        return service.getCart(id);
    }

    @GetMapping(value = "/delete_cart/{id}")
    public void deleteCart(@PathVariable Long id) {
        service.deleteCart(id);
    }

    @GetMapping(value = "/add_product_to_cart/{cartId}")
    public void addProductToCart(@PathVariable Long cartId, @RequestParam(name = "product_id") Long productId, @RequestParam Integer quantity) {
        service.addProductToCart(cartId, productId, quantity);
    }

    @GetMapping(value = "/remove_product_from_cart/{cartId}")
    public void removeProductFromCart(@PathVariable Long cartId, @RequestParam(name = "product_id") Long productId, @RequestParam Integer quantity) {
        service.removeProductFromCart(cartId, productId, quantity);
    }

    @GetMapping(value = "/delete_product_from_cart/{cartId}")
    public void deleteProductFromCart(@PathVariable Long cartId, @RequestParam(name = "product_id") Long productId) {
        service.deleteProductFromCart(cartId, productId);
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