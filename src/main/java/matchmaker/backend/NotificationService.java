package matchmaker.backend;

import matchmaker.backend.models.*;
import matchmaker.backend.repositories.CompanyRepository;
import matchmaker.backend.repositories.NotificationRepository;
import matchmaker.backend.repositories.UserRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    public void sendNotificationToAllFollowers(Company company, Notification notification) {
        for (Long follower : company.getFollowerIds()) {
            Notification newNotification = new Notification(notification);
            User user = userRepository.findById(follower).get();
            user.getNotifications().add(newNotification);
            userRepository.save(user);
        }
    }

    public void sendNotificationToUser(User user, Notification notification) {
        Notification newNotification = new Notification(notification);
        user.getNotifications().add(newNotification);
        userRepository.save(user);
    }

    public void sendSuccesfulCompanyGradeNotificationForOwner(User user){
        Notification notification = new Notification();
        notification.setTitle("Company creation request accepted!");
        notification.setDescription("Your request to grade your company request has been accepted. Welcome on the platform!");
        notification.setLink("/company/" + user.getDepartment().getParentCompany().getId());
        sendNotificationToUser(user, notification);
    }

    public void sendNotificationToAllUsers(Notification notification) {
        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            Notification newNotification = new Notification(notification);
            user.getNotifications().add(newNotification);
            userRepository.save(user);
        }
    }

    public void sendChallengeCreatedNotificationToAllCompanyFollowers(Challenge challenge) {
        Notification notification = new Notification();
        notification.setTitle("New Challenge Created by " + challenge.department.parentCompany.getName());
        notification.setDescription(challenge.getTitle());
        notification.setLink("/challenge/" + challenge.getId());
        sendNotificationToAllFollowers(challenge.getDepartment().getParentCompany(), notification);
    }

    public void sendChallengeUpdatedNotificationToAllCompanyFollowers(Challenge challenge) {
        Notification notification = new Notification();
        notification.setTitle("Challenge Updated by " + challenge.department.parentCompany.getName());
        notification.setDescription(challenge.getTitle());
        notification.setLink("/challenge/" + challenge.getId());
        sendNotificationToAllFollowers(challenge.getDepartment().getParentCompany(), notification);
    }

    public void sendUsersChosenChallengeNotification(ChallengeInput reaction){
        Notification notification = new Notification();
        notification.setTitle("Your idea has been chosen!");
        notification.setDescription("Your idea has been chosen by " + reaction.getChallenge().getDepartment().getParentCompany().getName() + "!");
        notification.setLink("/challenge/" + reaction.getChallenge().getId());
        sendNotificationToUser(reaction.getAuthor(), notification);
    }
}
