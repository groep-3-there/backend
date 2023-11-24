package matchmaker.backend.repositories;

import matchmaker.backend.models.Department;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
  Optional<Department> findById(Long id);
  Iterable<Department> findAllByParentCompanyId(Long id);

  Optional<Department> findByNameAndParentCompanyId(String name, Long id);
}
