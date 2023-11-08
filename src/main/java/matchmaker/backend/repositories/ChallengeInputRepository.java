package matchmaker.backend.repositories;

import matchmaker.backend.UnitTests.ChallengeInput;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChallengeInputRepository extends CrudRepository<ChallengeInput, Long> {
    List<ChallengeInput> findAllByChallengeId(Long challengeId);
}
