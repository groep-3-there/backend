package matchmaker.backend.repositories;

import matchmaker.backend.models.Challenge;
import matchmaker.backend.models.Department;
import matchmaker.backend.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
    Optional<Department> findById(Long id);
}
