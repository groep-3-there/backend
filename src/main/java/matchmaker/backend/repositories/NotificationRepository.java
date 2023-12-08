package matchmaker.backend.repositories;

import matchmaker.backend.models.Notification;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
}
