package spring.shop.exceptions;

public class CartNotFound extends RuntimeException {
    public CartNotFound(String message) {
        super(message);
    }
}
