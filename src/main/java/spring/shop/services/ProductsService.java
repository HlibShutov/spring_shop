package spring.shop.services;

import org.springframework.web.bind.annotation.RestController;
import spring.shop.models.Product;
import spring.shop.repositories.ProductsRepository;

import java.util.List;

@RestController
public class ProductsService {
    private final ProductsRepository repository;

    ProductsService(ProductsRepository repository) {
        this.repository = repository;
    }

    public Iterable<Product> getProducts() {
        return repository.findAll();
    }
}
