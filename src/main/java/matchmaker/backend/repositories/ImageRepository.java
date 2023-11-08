package matchmaker.backend.repositories;

import matchmaker.backend.UnitTests.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Long> {

    Iterable<Image> findAllByAttachmentForChallengeId(Long id);


}
