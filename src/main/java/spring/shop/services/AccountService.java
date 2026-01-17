package spring.shop.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.shop.models.Account;
import spring.shop.repositories.AccountRepository;

@Service
public class AccountService {
    public AccountService(AccountRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void createAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        repository.save(account);
    }
}
