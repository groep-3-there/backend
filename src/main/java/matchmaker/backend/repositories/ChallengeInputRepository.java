package matchmaker.backend.repositories;

import matchmaker.backend.models.ChallengeInput;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface ChallengeInputRepository extends CrudRepository<ChallengeInput, Long> {
  List<ChallengeInput> findAllByChallengeId(Long challengeId);

  Long countByCreatedAtBetween(LocalDate date, LocalDate localDate);
}
