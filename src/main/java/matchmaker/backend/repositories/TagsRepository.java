package matchmaker.backend.repositories;

import matchmaker.backend.models.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagsRepository extends CrudRepository<Tag, Long> {}
