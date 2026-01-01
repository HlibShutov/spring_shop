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
        +addProductToCart(Long id)
        +deleteCart(Long id)
    }
    class CartService {
        -CartRepository repository
        +getCart(Long id) Cart
        +createCart(Optional~Long~ customerId) Long
        +deleteProductFromCart(Long id)
        +addProductToCart(Long id)
        +deleteCart(Long id)
    }
    class CartRepository {
        <<Interface>>
    }
    CartController <.. CartService
    CartService <.. CartRepository
    CartRepository <|.. CrudRepository

    class Cart {
        -Long cartId
        -Map~Product, Integer~ products
        -Optional~Customer~ customer
        -BigDecimal cost
    }

    CartRepository *-- Product
    CartRepository *-- Cart
    Cart o-- Product

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