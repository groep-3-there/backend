package matchmaker.backend.repositories;

import matchmaker.backend.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
  Optional<User> findById(Long id);

  Optional<User> findByEmail(String email);

  Optional<User> findFirstByOrderByIdAsc();

  Optional<User> findByFirebaseId(String firebaseId);

  Iterable<User> findAllByDepartment_ParentCompany_Id(Long id);

  Iterable<User> findAllByDepartment_Id(Long id);
}
