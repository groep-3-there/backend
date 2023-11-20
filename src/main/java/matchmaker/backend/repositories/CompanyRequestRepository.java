package matchmaker.backend.repositories;

import matchmaker.backend.models.Company;
import matchmaker.backend.models.CompanyRequest;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CompanyRequestRepository extends CrudRepository<CompanyRequest, Long> {
    Page<CompanyRequest> findAll(Pageable pageable);
}
