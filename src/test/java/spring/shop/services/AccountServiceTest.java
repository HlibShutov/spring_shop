package spring.shop.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.shop.models.Account;
import spring.shop.repositories.AccountRepository;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Captor
    private ArgumentCaptor<String> passwordCaptor;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountRepository, passwordEncoder);
    }

    @Test
    public void testCreateAccount() {
        accountService.createAccount("username", "password");
        Mockito.verify(accountRepository).save(any(Account.class));
        Mockito.verify(passwordEncoder).encode(passwordCaptor.capture());
        Assertions.assertEquals("password", passwordCaptor.getValue());
    }
}