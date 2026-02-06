package spring.shop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spring.shop.exceptions.AccountNotFound;
import spring.shop.services.AccountService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountConroller {
    public AccountConroller(AccountService service) {
        this.service = service;
    }

    private final AccountService service;

    public record authDTO(String username, String password) {
    }
    public record jwtDTO(String access, String refresh) {
    }
    public record accessTokenRequest(String token) {
    }

    @PostMapping("/account")
    public void createAccount(@RequestBody authDTO request) {
        service.createAccount(request.username(), request.password());
    }

    @PostMapping("/login")
    public jwtDTO login(@RequestBody authDTO loginRequest) {
        return service.login(loginRequest.username(), loginRequest.password());
    }

    @PostMapping("/access_token")
    public String accessToken(@RequestBody accessTokenRequest refreshToken) {
        return service.accessToken(refreshToken.token());
    }

    @ExceptionHandler(AccountNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleAccountNotFound(AccountNotFound e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Account not found");
        errorResponse.put("message", e.getMessage());
        return errorResponse;
    }
}
