package spring.shop.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.shop.repositories.AccountRepository;

@Service
public class AccountDetailsService implements UserDetailsService {
    public AccountDetailsService(AccountRepository accountRepository) {
        this.repository = accountRepository;
    }

    private final AccountRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findById(username).orElseThrow(() -> new UsernameNotFoundException("not found"));
    }
}
