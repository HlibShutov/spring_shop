package spring.shop.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.shop.exceptions.CustomerNotFound;
import spring.shop.models.Customer;
import spring.shop.repositories.CustomerRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        customerService = new CustomerService(customerRepository);
    }

    @Test
    public void testGetCustomer() {
        Customer testCustomer = new Customer();
        Mockito
                .when(customerRepository.findById((long) 0))
                .thenReturn(Optional.of(testCustomer));
        Customer customer = customerService.getCustomer((long)0);
        Mockito.verify(customerRepository).findById((long)0);
        Assertions.assertEquals(testCustomer, customer);
    }

    @Test
    public void testGetProductNotExist() {
        Mockito
                .when(customerRepository.findById((long) 0))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(CustomerNotFound.class, () -> {
            customerService.getCustomer((long)0);
        });
        Mockito.verify(customerRepository).findById((long)0);
    }
}