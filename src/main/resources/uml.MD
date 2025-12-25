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