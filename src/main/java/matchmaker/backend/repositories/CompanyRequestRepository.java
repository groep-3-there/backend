package matchmaker.backend.repositories;

import matchmaker.backend.models.CompanyRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRequestRepository extends CrudRepository<CompanyRequest, Long> {
  Page<CompanyRequest> findAll(Pageable pageable);
}
