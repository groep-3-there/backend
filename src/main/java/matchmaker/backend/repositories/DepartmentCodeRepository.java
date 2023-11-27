package matchmaker.backend.repositories;

import matchmaker.backend.models.DepartmentCode;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DepartmentCodeRepository extends CrudRepository<DepartmentCode, Long> {
  Optional<DepartmentCode> findByDepartmentId(Long departmentId);

  Optional<DepartmentCode> findByCode(String code);
}
