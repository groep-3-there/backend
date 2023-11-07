package matchmaker.backend.repositories;

import matchmaker.backend.models.Challenge;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ChallengeRepository extends CrudRepository<Challenge, Long>, JpaSpecificationExecutor<Challenge> {
    Iterable<Challenge> findByAuthorId(Long authorId);
}
