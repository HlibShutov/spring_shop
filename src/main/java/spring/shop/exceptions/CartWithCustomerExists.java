package spring.shop.exceptions;

public class CartWithCustomerExists extends RuntimeException {
    public CartWithCustomerExists(String message) {
        super(message);
    }
}
