package spring.shop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    @ResponseBody
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
    @ResponseBody
    public Map<String, Object> getProduct(@PathVariable Long id) {
        Product product = service.getProduct(id);
        Map<String, Object> serializedProduct = new HashMap<>();
        serializedProduct.put("name", product.getName());
        serializedProduct.put("description", product.getDescription());
        serializedProduct.put("price", product.getPrice());
        serializedProduct.put("images", product.getImages());
        return serializedProduct;
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
