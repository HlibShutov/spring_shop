package spring.shop.repositories;

import org.springframework.data.repository.CrudRepository;
import spring.shop.models.Cart;

public interface CartRepository extends CrudRepository<Cart, Long> {
    boolean existsByCustomer_CustomerId(Long customerId);
}

