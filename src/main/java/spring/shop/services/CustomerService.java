package spring.shop.services;

import org.springframework.web.bind.annotation.RestController;
import spring.shop.exceptions.CustomerNotFound;
import spring.shop.exceptions.ProductNotFound;
import spring.shop.models.Customer;
import spring.shop.models.Product;
import spring.shop.repositories.CustomerRepository;

@RestController
public class CustomerService {
    private final CustomerRepository repository;

    CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer getCustomer(Long id) throws CustomerNotFound {
        return repository.findById(id).orElseThrow(() -> new CustomerNotFound("No such customer with ID %d".formatted(id)));
    }
}
