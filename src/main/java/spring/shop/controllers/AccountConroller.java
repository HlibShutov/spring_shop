package spring.shop.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring.shop.services.AccountService;

@RestController
public class AccountConroller {
    public AccountConroller(AccountService service, AuthenticationManager authenticationManager) {
        this.service = service;
        this.authenticationManager = authenticationManager;
    }

    private final AccountService service;
    private final AuthenticationManager authenticationManager;

    public record authDTO(String username, String password) {
    }

    @PostMapping("/create_account")
    public void createAccount(@RequestBody authDTO request) {
        service.createAccount(request.username(), request.password());
    }

    @PostMapping("/login")
    public String login(@RequestBody authDTO loginRequest) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);
        return "logged in";
    }
}
