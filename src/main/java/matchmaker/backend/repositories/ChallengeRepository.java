package matchmaker.backend.repositories;

import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.constants.ChallengeVisibility;
import matchmaker.backend.models.Challenge;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ChallengeRepository
    extends CrudRepository<Challenge, Long>, JpaSpecificationExecutor<Challenge> {
  Iterable<Challenge> findByAuthorId(Long authorId);

  Iterable<Challenge> findAllByDepartmentId(Long departmentId);

  Iterable<Challenge> findChallengeByDepartment_ParentCompanyId(Long parentCompanyId);

  List<Challenge> findChallengesByVisibilityIs(ChallengeVisibility status);

  void deleteAllByAuthorId(Long authorId);

  List<Challenge> findChallengesByVisibilityIsAndStatusIs(
      ChallengeVisibility aPublic, ChallengeStatus status);

  List<Challenge> findChallengesByVisibilityIsAndCreatedAt(
      ChallengeVisibility aPublic, LocalDate createdAtDate);

  List<Challenge> findChallengesByVisibilityIsAndStatusIsAndCreatedAt(
      ChallengeVisibility aPublic, ChallengeStatus status, LocalDate createdAtDate);
}
