package spring.shop.services;

import org.springframework.stereotype.Service;
import spring.shop.exceptions.ProductNotFound;
import spring.shop.models.Product;
import spring.shop.repositories.ProductsRepository;

@Service
public class ProductsService {
    private final ProductsRepository repository;

    ProductsService(ProductsRepository repository) {
        this.repository = repository;
    }

    public Iterable<Product> getProducts() {
        return repository.findAll();
    }
    public Product getProduct(Long id) throws ProductNotFound {
        return repository.findById(id).orElseThrow(() -> new ProductNotFound("No such product with ID %d".formatted(id)));
    }
}
