package spring.shop.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.shop.exceptions.*;
import spring.shop.models.*;

import static org.mockito.ArgumentMatchers.any;

import spring.shop.repositories.CartRepository;
import spring.shop.repositories.CustomerRepository;
import spring.shop.repositories.ProductsRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock
    private ProductsRepository productsRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CartRepository cartRepository;

    private CartService cartService;
    private Customer customer;
    private Account account;

    @BeforeEach
    public void setUp() {
        cartService = new CartService(cartRepository, customerRepository, productsRepository);
        customer = new Customer();
        customer.setCustomerId((long)0);
        account = new Account();
        account.setUsername("username");
        customer.setAccount(account);
    }

    @Test
    public void testGetCart() {
        Cart testCart = new Cart();
        testCart.setCustomer(customer);
        Mockito
                .when(cartRepository.findByCustomerAccountUsername("username"))
                .thenReturn(Optional.of(testCart));
        Cart cart = cartService.getCart("username");
        Assertions.assertEquals(testCart, cart);
    }

    @Test
    public void testGetCartNotExist() {
        Mockito
                .when(cartRepository.findByCustomerAccountUsername("username"))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(CartNotFound.class, () -> {
            cartService.getCart("username");
        });
    }

    @Test
    public void testCreateCart() {
        Mockito
                .when(customerRepository.findByAccountUsername("username"))
                .thenReturn(Optional.of(customer));
        cartService.createCart("username");
        Mockito.verify(cartRepository).save(any(Cart.class));
    }

    @Test
    public void testCreateCartWithConflictingCustomer() {
        Mockito
                .when(customerRepository.findByAccountUsername("username"))
                .thenReturn(Optional.of(customer));
        Mockito.when(cartRepository.existsByCustomerCustomerId((long) 0)).thenReturn(true);
        Assertions.assertThrows(CartWithCustomerExists.class, () -> cartService.createCart("username"));
    }

    @Test
    public void testCreateCartWithInvalidCustomer() {
        Assertions.assertThrows(CustomerNotFound.class, () -> cartService.createCart("username1"));
    }

    @Test
    public void testAddProductToCart() {
        Cart cart = new Cart(null);
        Product product = new Product();
        product.setPrice(BigDecimal.TEN);
        Mockito.when(productsRepository.findById((long) 0)).thenReturn(Optional.of(product));
        Mockito
                .when(cartRepository.findByCustomerAccountUsername("username"))
                .thenReturn(Optional.of(cart));
        cartService.addProductToCart("username", (long) 0, 5);
        CartItem cartItem = cart.getItems().get(0);
        Assertions.assertEquals(product, cartItem.getProduct());
        Assertions.assertEquals(5, cartItem.getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(50), cart.getCost());
    }

    @Test
    public void testAddProductToCartItemExists() {
        Cart cart = new Cart(null);
        Product product = new Product();
        product.setProductId((long) 0);
        product.setPrice(BigDecimal.TEN);
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(3);
        cart.setItems(List.of(item));
        cart.setCost(BigDecimal.valueOf(30));
        Mockito.when(productsRepository.findById((long) 0)).thenReturn(Optional.of(product));
        Mockito
                .when(cartRepository.findByCustomerAccountUsername("username"))
                .thenReturn(Optional.of(cart));

        cartService.addProductToCart("username", (long) 0, 5);
        CartItem cartItem = cart.getItems().get(0);
        Assertions.assertEquals(product, cartItem.getProduct());
        Assertions.assertEquals(8, cartItem.getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(80), cart.getCost());
    }

    @Test
    public void testAddProductToCartInvalidCart() {
        Product product = new Product();
        Assertions.assertThrows(CartNotFound.class, () -> cartService.addProductToCart("username1", (long) 0, 5));
    }

    @Test
    public void testAddProductToCartInvalidProduct() {
        Cart cart = new Cart();
        Mockito
                .when(cartRepository.findByCustomerAccountUsername("username"))
                .thenReturn(Optional.of(cart));

        Assertions.assertThrows(ProductNotFound.class, () -> cartService.addProductToCart("username", (long) 0, 5));
    }

    @Test
    public void testRemoveProductFromCart() {
        Cart cart = new Cart(null);
        Product product = new Product();
        product.setProductId((long) 0);
        product.setPrice(BigDecimal.TEN);
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(3);
        cart.setItems(List.of(item));
        cart.setCost(BigDecimal.valueOf(30));

        Mockito.when(productsRepository.findById((long) 0)).thenReturn(Optional.of(product));
        Mockito
                .when(cartRepository.findByCustomerAccountUsername("username"))
                .thenReturn(Optional.of(cart));

        cartService.removeProductFromCart("username", (long) 0, 1);
        CartItem cartItem = cart.getItems().get(0);

        Assertions.assertEquals(2, cartItem.getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(20), cart.getCost());
    }

    @Test
    public void testRemoveItemFromCart() {
        Cart cart = new Cart(null);
        Product product = new Product();
        product.setProductId((long) 0);
        product.setPrice(BigDecimal.TEN);
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(3);
        List<CartItem> items = new ArrayList<>();
        items.add(item);
        cart.setItems(items);
        cart.setCost(BigDecimal.valueOf(30));

        Mockito.when(productsRepository.findById((long) 0)).thenReturn(Optional.of(product));
        Mockito
                .when(cartRepository.findByCustomerAccountUsername("username"))
                .thenReturn(Optional.of(cart));

        cartService.removeProductFromCart("username", (long) 0, 3);
        Assertions.assertTrue(cart.getItems().isEmpty());
        Assertions.assertEquals(BigDecimal.ZERO, cart.getCost());
    }

    @Test
    public void testDeleteProductFromCart() {
        Cart cart = new Cart(null);
        Product product = new Product();
        product.setProductId((long) 0);
        product.setPrice(BigDecimal.TEN);
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(3);
        List<CartItem> items = new ArrayList<>();
        items.add(item);
        cart.setItems(items);
        cart.setCost(BigDecimal.valueOf(30));

        Mockito.when(productsRepository.findById((long) 0)).thenReturn(Optional.of(product));
        Mockito
                .when(cartRepository.findByCustomerAccountUsername("username"))
                .thenReturn(Optional.of(cart));


        cartService.deleteProductFromCart("username", (long) 0);
        Assertions.assertTrue(cart.getItems().isEmpty());
        Assertions.assertEquals(BigDecimal.ZERO, cart.getCost());
    }

    @Test
    public void testDeleteProductFromCartInvalidProduct() {
        Cart cart = new Cart(null);

        Mockito.when(productsRepository.findById((long) 0)).thenReturn(Optional.empty());
        Mockito
                .when(cartRepository.findByCustomerAccountUsername("username"))
                .thenReturn(Optional.of(cart));


        Assertions.assertThrows(ProductNotFound.class, () -> cartService.deleteProductFromCart("username", (long) 0));
    }

    @Test
    public void testRemoveProductFromCartInvalidQuantity() {
        Cart cart = new Cart(null);
        Product product = new Product();
        product.setProductId((long) 0);
        product.setPrice(BigDecimal.TEN);
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(3);
        List<CartItem> items = new ArrayList<>();
        items.add(item);
        cart.setItems(items);
        cart.setCost(BigDecimal.valueOf(30));

        Mockito.when(productsRepository.findById((long) 0)).thenReturn(Optional.of(product));
        Mockito
                .when(cartRepository.findByCustomerAccountUsername("username"))
                .thenReturn(Optional.of(cart));

        Assertions.assertThrows(InvalidAmount.class, () -> cartService.removeProductFromCart("username", (long) 0, 5));
    }
}