package spring.shop.repositories;

import org.springframework.data.repository.CrudRepository;
import spring.shop.models.Image;

public interface ImagesRepository extends CrudRepository<Image, Long> {

}
