package spring.shop.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import spring.shop.exceptions.*;
import spring.shop.models.Cart;
import spring.shop.services.CartService;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Cart", description = "Shopping cart management")
@RestController
@RequestMapping("/cart")
public class CartController {
    public CartController(CartService service) {
        this.service = service;
    }

    private CartService service;

    @Operation(summary = "Create a cart", description = "Creates a new shopping cart for the authenticated user.")
    @ApiResponses({ @ApiResponse( responseCode = "201", description = "Cart successfully created" ) })
    @PostMapping(value = "/create", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createCart(Authentication authentication) {
        return service.createCart(authentication.getName());
    }

    @Operation(summary = "Get current cart", description = "Returns the shopping cart belonging to the authenticated user.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Cart successfully retrieved"), @ApiResponse(responseCode = "404", description = "Cart not found") })
    @GetMapping(value = "/", produces = "application/json")
    public Cart getCart(Authentication authentication) {
        return service.getCart(authentication.getName());
    }

    @Operation(summary = "Delete current cart", description = "Deletes the shopping cart belonging to the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Cart successfully deleted")
    @DeleteMapping(value = "/")
    public void deleteCart(Authentication authentication) {
        service.deleteCart(authentication.getName());
    }

    @Operation(summary = "Add product to cart", description = "Adds a specified quantity of a product to the authenticated user's cart.")
    @ApiResponses({ @ApiResponse( responseCode = "200", description = "Product successfully added"), @ApiResponse(responseCode = "404", description = "Cart or product not found") })
    @PutMapping(value = "/product/{productId}")
    public void addProductToCart(Authentication authentication, @PathVariable Long productId, @RequestParam Integer quantity) {
        service.addProductToCart(authentication.getName(), productId, quantity);
    }

    @Operation(summary = "Decrease product quantity", description = "Decreases the quantity of a product in the authenticated user's cart.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Product quantity successfully decreased"), @ApiResponse(responseCode = "404", description = "Cart item not found or invalid quantity") })
    @PatchMapping(value = "/product/{productId}")
    public void removeProductFromCart(@PathVariable Long productId, @RequestParam Integer quantity, Authentication authentication) {
        service.removeProductFromCart(authentication.getName(), productId, quantity);
    }

    @Operation(summary = "Remove product from cart", description = "Completely removes a product from the authenticated user's cart.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Product successfully removed"), @ApiResponse(responseCode = "404", description = "Cart item not found") })
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