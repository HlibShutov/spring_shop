package spring.shop.repositories;

import org.springframework.data.repository.CrudRepository;
import spring.shop.models.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

}
