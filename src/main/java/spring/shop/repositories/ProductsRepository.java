package spring.shop.repositories;

import org.springframework.data.repository.CrudRepository;
import spring.shop.models.Product;

public interface ProductsRepository extends CrudRepository<Product, Long> {

}