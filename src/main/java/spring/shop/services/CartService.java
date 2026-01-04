package spring.shop.services;

import org.springframework.stereotype.Service;
import spring.shop.exceptions.*;
import spring.shop.models.Cart;
import spring.shop.models.CartItem;
import spring.shop.models.Customer;
import spring.shop.models.Product;
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
    public Long createCart(Long id) {
        Customer customer = null;
        if (id != null) {
            customer = customerRepository.findById(id)
                    .orElseThrow(() -> new CustomerNotFound("No such customer with ID %d".formatted(id)));
            if (cartRepository.existsByCustomer_CustomerId(id)) throw new CartWithCustomerExists("Cart with customer with ID %d already exists".formatted(id));
        }

        Cart cart = new Cart(customer);
        cartRepository.save(cart);
        return cart.getCartId();
    }
    public Cart getCart(Long id) throws CartNotFound {
        return cartRepository.findById(id).orElseThrow(() -> new CartNotFound("No such cart with ID %d".formatted(id)));
    }

    public void deleteCart(Long id) throws CartNotFound {
        cartRepository.deleteById(id);
    }

    public void addProductToCart(Long cartId, Long productId, Integer quantity) throws CartNotFound, ProductNotFound {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFound("No such cart with ID %d".formatted(cartId)));
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
    public void removeProductFromCart(Long cartId, Long productId, Integer quantity) throws CartNotFound, ProductNotFound, CartItemNotFound {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFound("No such cart with ID %d".formatted(cartId)));
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
    public void deleteProductFromCart(Long cartId, Long productId) throws CartNotFound, ProductNotFound, CartItemNotFound {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFound("No such cart with ID %d".formatted(cartId)));
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
