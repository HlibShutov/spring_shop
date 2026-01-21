package spring.shop.repositories;

import org.springframework.data.repository.CrudRepository;
import spring.shop.models.Cart;

import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, Long> {
    boolean existsByCustomerCustomerId(Long customerId);
    Optional<Cart> findByCustomerAccountUsername(String customer);
}

