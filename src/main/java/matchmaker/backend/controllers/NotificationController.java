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

    @GetMapping("/notifications/matchmaker")
    public Iterable<Notification> getNotifications(User currentUser) {
        User user = userRepository.findById(1L).get();
        return user.notifications;
    }

    @PutMapping("/notifications/read/{id}")
    public void readNotification(@PathVariable Long id) {
        Notification notification = notificationRepository.findById(id).get();
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
