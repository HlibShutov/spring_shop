package spring.shop.services;

import org.springframework.stereotype.Service;
import spring.shop.exceptions.CustomerNotFound;
import spring.shop.models.Customer;
import spring.shop.repositories.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository repository;

    CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer getCustomer(Long id) throws CustomerNotFound {
        return repository.findById(id).orElseThrow(() -> new CustomerNotFound("No such customer with ID %d".formatted(id)));
    }
}
