package spring.shop.repositories;

import org.springframework.data.repository.CrudRepository;
import spring.shop.models.Account;
import spring.shop.models.Cart;

public interface AccountRepository extends CrudRepository<Account, String> {

}

