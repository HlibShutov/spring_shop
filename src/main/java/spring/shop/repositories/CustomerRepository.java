package spring.shop.repositories;

import org.springframework.data.repository.CrudRepository;
import spring.shop.models.Customer;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Optional<Customer> findByAccountUsername(String username);
}
