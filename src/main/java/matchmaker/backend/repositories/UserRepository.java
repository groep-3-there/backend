package matchmaker.backend.repositories;

import matchmaker.backend.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findFirstByOrderByIdAsc();
}
