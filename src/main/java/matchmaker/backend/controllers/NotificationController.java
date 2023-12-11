package matchmaker.backend.controllers;

import matchmaker.backend.NotificationService;
import matchmaker.backend.models.Company;
import matchmaker.backend.models.Notification;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.CompanyRepository;
import matchmaker.backend.repositories.NotificationRepository;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @PutMapping("/notifications/read/{id}")
    public boolean readNotification(@PathVariable Long id,@RequestAttribute(name = "loggedInUser") User currentUser) {
        Notification notification = notificationRepository.findById(id).get();
        if(currentUser == null) {
            return false;
        }
        currentUser.notifications.forEach((n) -> {
            if(n.id.equals(id)) {
                notification.setRead(true);
                notificationRepository.save(notification);
            }
        });
        return true;

    }
}
