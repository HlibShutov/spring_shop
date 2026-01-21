package spring.shop.services;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import spring.shop.exceptions.*;
import spring.shop.models.*;
import spring.shop.repositories.CartRepository;
import spring.shop.repositories.CustomerRepository;
import spring.shop.repositories.ProductsRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final ProductsRepository productsRepository;

    public CartService(CartRepository cartRepository, CustomerRepository customerRepository, ProductsRepository productsRepository) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.productsRepository = productsRepository;
    }
    public Long createCart(String username) {
        Optional<Customer> customerOptional = customerRepository.findByAccountUsername(username);
        if (customerOptional.isEmpty()) throw new CustomerNotFound("customer not found for username %s".formatted(username));
        Customer customer = customerOptional.get();
        if (cartRepository.existsByCustomerCustomerId(customer.getCustomerId())) throw new CartWithCustomerExists("Cart with customer with ID %d already exists".formatted(customer.getCustomerId()));

        Cart cart = new Cart(customer);
        cartRepository.save(cart);
        return cart.getCartId();
    }
    public Cart getCart(String username) throws CartNotFound {
        return cartRepository.findByCustomerAccountUsername(username).orElseThrow(() -> new CartNotFound("no such cart for %s".formatted(username)));
    }

    public void deleteCart(String username) throws CartNotFound {
        Cart cart = cartRepository.findByCustomerAccountUsername(username).orElseThrow(() -> new CartNotFound("no such cart for %s".formatted(username)));
        cartRepository.deleteById(cart.getCartId());
    }

    public void addProductToCart(String username, Long productId, Integer quantity) throws CartNotFound, ProductNotFound {
        Cart cart = cartRepository.findByCustomerAccountUsername(username).orElseThrow(() -> new CartNotFound("No such cart for username %s".formatted(username)));
        Product product = productsRepository.findById(productId).orElseThrow(() -> new ProductNotFound("No such product with ID %d".formatted(productId)));
        Optional<CartItem> cartItem = cart.getItems().stream().filter(item -> item.getProduct().getProductId().equals(productId)).findFirst();
        if (cartItem.isEmpty()) {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
            cart.setCost(cart.getCost().add(product.getPrice().multiply(BigDecimal.valueOf(quantity))));
        } else {
            CartItem existingItem = cartItem.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cart.setCost(cart.getCost().add(product.getPrice().multiply(BigDecimal.valueOf(quantity))));
        }
        cartRepository.save(cart);
    }
    public void removeProductFromCart(String username, Long productId, Integer quantity) throws CartNotFound, ProductNotFound, CartItemNotFound {
        Cart cart = cartRepository.findByCustomerAccountUsername(username).orElseThrow(() -> new CartNotFound("No such cart for username %s".formatted(username)));
        Product product = productsRepository.findById(productId).orElseThrow(() -> new ProductNotFound("No such product with ID %d".formatted(productId)));
        Optional<CartItem> cartItem = cart.getItems().stream().filter(item -> item.getProduct().getProductId().equals(productId)).findFirst();
        if (cartItem.isPresent()) {
            CartItem existingItem = cartItem.get();
            int newQuantity = existingItem.getQuantity() - quantity;
            if (newQuantity > 0) existingItem.setQuantity(newQuantity);
            else if (newQuantity == 0) {
                cart.getItems().remove(existingItem);
            }
            else throw new InvalidAmount("Not enough quantity (%d) of product with ID %d in cart".formatted(existingItem.getQuantity(), productId));
            cart.setCost(cart.getCost().subtract(product.getPrice().multiply(BigDecimal.valueOf(quantity))));
        } else {
            throw new CartItemNotFound("No such cart item for product with ID %d".formatted(productId));
        }
        cartRepository.save(cart);
    }
    public void deleteProductFromCart(String username, Long productId) throws CartNotFound, ProductNotFound, CartItemNotFound {
        Cart cart = cartRepository.findByCustomerAccountUsername(username).orElseThrow(() -> new CartNotFound("No such cart for username %s".formatted(username)));
        Product product = productsRepository.findById(productId).orElseThrow(() -> new ProductNotFound("No such product with ID %d".formatted(productId)));
        Optional<CartItem> cartItem = cart.getItems().stream().filter(item -> item.getProduct().getProductId().equals(productId)).findFirst();
        if (cartItem.isPresent()) {
            CartItem existingItem = cartItem.get();
            cart.getItems().remove(existingItem);
            cart.setCost(cart.getCost().subtract(product.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity()))));
        } else {
            throw new CartItemNotFound("No such cart item for product with ID %d".formatted(productId));
        }
        cartRepository.save(cart);
    }
}
