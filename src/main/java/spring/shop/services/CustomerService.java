package spring.shop.services;

import org.springframework.stereotype.Service;
import spring.shop.exceptions.AccountNotFound;
import spring.shop.exceptions.CustomerNotFound;
import spring.shop.models.Account;
import spring.shop.models.Customer;
import spring.shop.repositories.AccountRepository;
import spring.shop.repositories.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository repository;
    private final AccountRepository accountRepository;

    CustomerService(CustomerRepository repository, AccountRepository accountRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    public Customer getCustomer(String username) throws CustomerNotFound {
        return repository.findByAccountUsername(username).orElseThrow(() -> new CustomerNotFound("No such customer for username %s".formatted(username)));
    }

    public Long createCustomer(String username, Customer customer) {
        customer.setAccount(accountRepository.findById(username).orElseThrow(() -> new AccountNotFound("account not found for username %s".formatted(username))));
        Account account = accountRepository.findById(username).get();
        account.setCustomer(customer);
        repository.save(customer);
        accountRepository.save(account);
        return customer.getCustomerId();
    }
}
