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
        +getCart(Long id) Cart
        +createCart(Optional~Long~ customerId) Long
        +deleteProductFromCart(Long id)
        +addProductToCart(Long cartId, Long productId, Integer quantity)
        +removeProductFromCart(Long cartId, Long productId, Integer quantity)
        +deleteProductFromCart(Long cartId, Long productId)
        +deleteCart(Long id)
    }
    class CartService {
        -CartRepository repository
        +getCart(Long id) Cart
        +createCart(Optional~Long~ customerId) Long
        +deleteProductFromCart(Long id)
        +addProductToCart(Long cartId, Long productId, Integer quantity)
        +removeProductFromCart(Long cartId, Long productId, Integer quantity)
        +deleteProductFromCart(Long cartId, Long productId)
        +deleteCart(Long id)
    }
    class CartRepository {
        <<Interface>>
        existsByCustomer_CustomerId(Long customerId) boolean
    }
    CartController <.. CartService
    CartService <.. CartRepository
    CartRepository <|.. CrudRepository

    class Cart {
        -Long cartId
        -List~Product~ items
        -Optional~Customer~ customer
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
        +getCustomer(Long id) Customer
    }
    class CustomerService {
        -CustomerRepository repository
        +getCustomer(Long id) Customer
    }
    class CustomerRepository {
        <<Interface>>
    }
    CustomerRepository <|.. CrudRepository
    CustomerService <.. CustomerRepository
    CustomerController <.. CustomerService
    CustomerRepository *-- Customer