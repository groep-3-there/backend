package matchmaker.backend.repositories;

import matchmaker.backend.models.Branch;
import matchmaker.backend.models.Company;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface CompanyRepository extends CrudRepository<Company, Long> {
  long countByCreatedAtBetween(LocalDate from, LocalDate till);

  List<Object> findByName(String graphDataCompany);

  Long countByBranch(Branch branch);
}
