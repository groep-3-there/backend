package matchmaker.backend.repositories;

import matchmaker.backend.models.Department;
import matchmaker.backend.models.Image;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends CrudRepository<Image, Long> {

    Iterable<Image> findAllByAttachmentForChallengeId(Long id);


}
