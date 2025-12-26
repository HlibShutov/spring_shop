package spring.shop.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.shop.exceptions.ProductNotFound;
import spring.shop.models.Product;
import spring.shop.repositories.ProductsRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductsServiceTest {
    @Mock
    private ProductsRepository productsRepository;

    private ProductsService productsService;

    @BeforeEach
    public void setUp() {
        productsService = new ProductsService(productsRepository);
    }

    @Test
    public void testGetProducts() {
        Iterable<Product> testProducts = List.of(new Product(), new Product(), new Product());
        Mockito
                .when(productsRepository.findAll())
                .thenReturn(testProducts);
        Iterable<Product> products = productsService.getProducts();
        Mockito.verify(productsRepository).findAll();
        Assertions.assertEquals(testProducts, products);
    }

    @Test
    public void testGetProduct() {
        Product testProduct = new Product();
        Mockito
                .when(productsRepository.findById((long) 0))
                .thenReturn(Optional.of(testProduct));
        Product product = productsService.getProduct((long)0);
        Mockito.verify(productsRepository).findById((long)0);
        Assertions.assertEquals(testProduct, product);
    }

    @Test
    public void testGetProductNotExist() {
        Mockito
                .when(productsRepository.findById((long) 0))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ProductNotFound.class, () -> {
            productsService.getProduct((long)0);
        });
        Mockito.verify(productsRepository).findById((long)0);
    }
}