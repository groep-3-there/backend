package matchmaker.backend.repositories;

import matchmaker.backend.UnitTests.Challenge;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends CrudRepository<Challenge, Long>, JpaSpecificationExecutor<Challenge> {
    Iterable<Challenge> findByAuthorId(Long authorId);
}
