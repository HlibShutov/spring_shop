package spring.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import spring.shop.models.Product;
import spring.shop.services.ProductsService;

@RestController
public class ProductsController {
    private final ProductsService service;

    public ProductsController(ProductsService productsService) {
        this.service = productsService;
    }
    @GetMapping(value = "/products", produces = "application/json")
    @ResponseBody
    public Iterable<Product> getProducts() {
        return service.getProducts();
    }
}
