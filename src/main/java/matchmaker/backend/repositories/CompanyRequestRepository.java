package matchmaker.backend.repositories;

import matchmaker.backend.models.CompanyRequest;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRequestRepository extends CrudRepository<CompanyRequest, Long> {
}
