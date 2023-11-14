package matchmaker.backend.repositories;

import matchmaker.backend.models.Company;
import matchmaker.backend.models.Department;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, Long>{

}
