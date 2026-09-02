package spring.shop.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spring.shop.exceptions.AccountNotFound;
import spring.shop.services.AccountService;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Authentication", description = "Account registration and JWT authentication")
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

    @Operation(summary = "Create an account", description = "Creates a new user account using a username and password.")
    @ApiResponses({ @ApiResponse( responseCode = "200", description = "Account successfully created" ) })
    @PostMapping("/account")
    public void createAccount(@RequestBody authDTO request) {
        service.createAccount(request.username(), request.password());
    }

    @Operation(summary = "Login", description = "Authenticates a user and returns an access token and refresh token.")
    @ApiResponses({ @ApiResponse( responseCode = "200", description = "Authentication successful" ), @ApiResponse( responseCode = "404", description = "Account not found" ) })
    @PostMapping("/login")
    public jwtDTO login(@RequestBody authDTO loginRequest) {
        return service.login(loginRequest.username(), loginRequest.password());
    }

    @Operation(summary = "Refresh access token", description = "Generates a new access token using a valid refresh token.")
    @ApiResponses({ @ApiResponse( responseCode = "200", description = "New access token generated" ) })
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
