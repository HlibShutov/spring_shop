package spring.shop.services;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.shop.controllers.AccountConroller;
import spring.shop.exceptions.AccountNotFound;
import spring.shop.models.Account;
import spring.shop.repositories.AccountRepository;

@Service
public class AccountService {
    public AccountService(AccountRepository repository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void createAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        repository.save(account);
    }
    public AccountConroller.jwtDTO login(String username, String password) throws AccountNotFound{
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);
        Account account = repository.findById(username).orElseThrow(() -> new AccountNotFound("account with username %s not found".formatted(username)));
        String access = jwtService.getAccessToken(account);
        String refresh = jwtService.getAccessToken(account);
        return new AccountConroller.jwtDTO(access, refresh);
    }
    public String accessToken(String refresh) throws AccountNotFound {
        String username = jwtService.getUsername(refresh);
        Account account = repository.findById(username).orElseThrow(() -> new AccountNotFound("account with username %s not found".formatted(username)));
        if (jwtService.checkToken(refresh, username)) {
            return jwtService.getAccessToken(account);
        }
        throw new AccessDeniedException("access denied");
    }
}
