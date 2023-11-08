package matchmaker.backend.repositories;

import matchmaker.backend.UnitTests.Branch;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BranchRepository extends CrudRepository<Branch, Long> {
    Optional<Branch> findById(Long id);
    Iterable<Branch> findAll();
}