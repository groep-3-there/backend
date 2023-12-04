package matchmaker.backend.repositories;

import matchmaker.backend.models.Company;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface CompanyRepository extends CrudRepository<Company, Long> {
    Iterable<Company> findCompaniesByCreatedAt(LocalDate date);

    long countByCreatedAtBetween(LocalDate from, LocalDate till);
}
