classDiagram
class ProductsController {
-ProductsService service
+getProducts() List~Map<String, Object>~
+getProduct(Long id) Map~String, Object~
}
class ProductsService {
-ProductsRepository repository
+getProducts() Iterable~Product~
+getProduct(Long id) Product
}
class ProductsRepository {
<<Interface>>
}
class CrudRepository {
<<Interface>>
}
ProductsController <.. ProductsService
ProductsService <.. ProductsRepository
ProductsRepository <|.. CrudRepository

    class Product {
        -Long productId
        -String name
        -String description
        -List~Image~ images
        -BigDecimal price;
        -Integer quantity;
    }
    class Image {
        -Long imageId
        -Product product
        -String path
    }
    ProductsRepository *-- Product
    ProductsRepository *-- Image
    Product o-- Image

    class CartController {
        -CartService service
        +getCart(Authentication authentication) Cart
        +createCart(Authentication authentication) Long
        +deleteProductFromCart(Authentication authentication, Long id)
        +addProductToCart(Authentication authentication, Long productId, Integer quantity)
        +removeProductFromCart(Authentication authentication, Long productId, Integer quantity)
        +deleteProductFromCart(Authentication authentication, Long productId)
        +deleteCart(Long id)
    }
    class CartService {
        -CartRepository repository
        +getCart(String username) Cart
        +createCart(String username) Long
        +deleteProductFromCart(String username, Long id)
        +addProductToCart(String username, Long productId, Integer quantity)
        +removeProductFromCart(String username, Long productId, Integer quantity)
        +deleteProductFromCart(String username, Long productId)
        +deleteCart(String username)
    }
    class CartRepository {
        <<Interface>>
        existsByCustomerCustomerId(Long customerId) boolean
	findByCustomerAccountUsername(String customer) Optional~Cart~

    }
    CartController <.. CartService
    CartService <.. CartRepository
    CartRepository <|.. CrudRepository

    class Cart {
        -Long cartId
        -List~Product~ items
        -Customer customer
        -BigDecimal cost
    }
    class CartItem {
        -Long cartItemId
        -Product product
        -Integer quantity
    }

    CartRepository *-- Cart
    CartItem o-- Product
    Cart *-- CartItem

    class Customer {
        -Long customerId
        -String name
        -String lastname
        -String email
        -Address address
        -Account account
    }
    class Address {
        -Long addressId
        -String address
        -String postalCode
        -String additionalInfo
    }

    Customer o-- Address
    Cart o-- Customer

    class CustomerController {
        -CustomerService service
        +getCustomer(Authentication authentication) Customer
        +createCustomer(Authentication authentication, Customer customer) Long
    }
    class CustomerService {
        -CustomerRepository repository
        -AccountRepository AccountRepository
        +getCustomer(String username) Customer
        +createCustomer(String username, Customer customer) Long
    }
    class CustomerRepository {
        <<Interface>>
	findByAccountUsername(String username) Optional~Customer~
    }
    CustomerRepository <|.. CrudRepository
    CustomerService <.. CustomerRepository
    CustomerService <.. AccountRepository
    CustomerController <.. CustomerService
    CustomerRepository *-- Customer

    class Account {
        -String username
        -String password
        -Customer customer
    }

    Customer o--o Account
    
    class AccountRepository {
        <<Interface>>
    }
    class AccountService {
        -AccountRepository repository
        +createAccount(String username, String password)
        +login(String username, String password) jwtDTO
        +accessToken(String refresh) String
    }
    class authDTO {
        -String username
        -String password
    }
    class jwtDTO {
        -String access
        -String refresh
    }
    class accessTokenRequest {
        -String token
    }
    class AccountController {
        -AccountService service
        +createAccount(authDTO request)
        +login(authDTO loginRequest) jwtDTO
        +accessToken(accessTokenRequest refreshToken) String
    }
    AccountRepository <|.. CrudRepository
    AccountService <.. AccountRepository
    AccountController <.. AccountService
    AccountRepository *-- Account
    AccountController *-- authDTO
    AccountController *-- jwtDTO
    AccountController *-- accessTokenRequest

    class JwtService {
        -long accessTokenExpiration;
        -long refreshTokenExpiration;
        -String secretKey;

        +getAccessToken(Account account) String
        +getRefreshToken(Account account) String
	    -getSigningKey() SecretKey
	    +checkToken(String token, String username) boolean
	    +getUsername(String token) String
	    +getClaims(String token) Claims
    }
    JwtService o-- Account