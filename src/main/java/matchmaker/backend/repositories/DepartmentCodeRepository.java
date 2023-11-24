package matchmaker.backend.repositories;

import matchmaker.backend.models.DepartmentCode;
import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface DepartmentCodeRepository extends CrudRepository<DepartmentCode, Long> {
  Optional<DepartmentCode> findByDepartmentId(Long departmentId);
  Optional<DepartmentCode> findByCode(String code);
}
