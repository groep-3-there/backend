package matchmaker.backend.repositories;

import matchmaker.backend.models.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Long> {
}
