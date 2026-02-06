package spring.shop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import spring.shop.exceptions.CartNotFound;
import spring.shop.exceptions.ProductNotFound;
import spring.shop.models.Product;
import spring.shop.services.ProductsService;

import java.util.*;

@RestController
public class ProductsController {
    private final ProductsService service;

    public ProductsController(ProductsService productsService) {
        this.service = productsService;
    }
    @GetMapping(value = "/products", produces = "application/json")
    public List<Map<String, Object>> getProducts() {
        Iterable<Product> products = service.getProducts();
        List<Map<String, Object>> response = new ArrayList<>();
        products.forEach(product -> {
            Map<String, Object> serializedProduct = new HashMap<>();
            serializedProduct.put("productId", product.getProductId());
            serializedProduct.put("name", product.getName());
            serializedProduct.put("description", product.getDescription());
            serializedProduct.put("price", product.getPrice());
            serializedProduct.put("images", product.getImages());
            response.add(serializedProduct);
        });
        return response;
    }

    @GetMapping(value = "/product/{id}", produces = "application/json")
    public Map<String, Object> getProduct(@PathVariable Long id) {
        Product product = service.getProduct(id);
        Map<String, Object> serializedProduct = new HashMap<>();
        serializedProduct.put("name", product.getName());
        serializedProduct.put("description", product.getDescription());
        serializedProduct.put("price", product.getPrice());
        serializedProduct.put("images", product.getImages());
        return serializedProduct;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/create_product", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public long createProduct(@RequestBody Product product) {
        return service.createProduct(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/delete_product/{id}", produces = "application/json")
    public void deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
    }

    @ExceptionHandler(ProductNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleProductNotFound(ProductNotFound e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Product not found");
        errorResponse.put("message", e.getMessage());
        return errorResponse;
    }
}
