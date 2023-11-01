package matchmaker.backend.repositories;

import matchmaker.backend.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, Long> {
}