package matchmaker.backend.repositories;

import matchmaker.backend.UnitTests.Department;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
    Optional<Department> findById(Long id);
}
