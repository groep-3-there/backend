package matchmaker.backend.repositories;

import matchmaker.backend.models.Challenge;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ChallengeRepository extends CrudRepository<Challenge, Long> {
    Iterable<Challenge> findByAuthorId(UUID author_id);
}
