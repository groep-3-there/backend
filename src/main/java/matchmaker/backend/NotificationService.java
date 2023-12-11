package matchmaker.backend;

import com.mailjet.client.errors.MailjetException;
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

    @Autowired
    private EmailService emailService;

    public void sendNotificationToAllFollowers(Company company, Notification notification) {
        for (Long follower : company.getFollowerIds()) {
            Notification newNotification = new Notification(notification);
            User user = userRepository.findById(follower).get();
            sendNotificationToUser(user, newNotification);
        }
    }

    public void sendNotificationToUser(User user, Notification notification) {
        Notification newNotification = new Notification(notification);
        user.sendNotification(newNotification);
        //Don't send email to test users
        if(!user.email.contains("@email")){
            try {
                emailService.sendEmail(user.getEmail(), user.getName(), notification.getTitle(), notification.getDescription(), "Bekijk", "https://matchmakergroep3.nl" + notification.getLink());
            } catch (MailjetException e) {
                e.printStackTrace();
            }
        }

        userRepository.save(user);
    }

    public void sendSuccesfulCompanyGradeNotificationForOwner(User user){
        Notification notification = new Notification();
        notification.setTitle("✅Uw bedrijfsaanvraag is goedgekeurd!");
        notification.setDescription("Uw bedrijfsaanvraag is bekeken en goedgekeurd door een MatchMaker. U kunt nu het platform gebruiken.");
        notification.setLink("/company/" + user.getDepartment().getParentCompany().getId());
        sendNotificationToUser(user, notification);
    }
    public void sendRejectedCompanyGradeNotificationForOwner(User user){
        Notification notification = new Notification();
        notification.setTitle("❌Uw bedrijfsaanvraag is afgekeurd");
        notification.setDescription("Uw bedrijfsaanvraag is bekeken en afgekeurd door een MatchMaker. U kunt een nieuwe aanvraag doen of contact opnemen met MatchMaker.");
        notification.setLink("");
        sendNotificationToUser(user, notification);
    }

    public void sendNotificationToAllUsers(Notification notification) {
        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            Notification newNotification = new Notification(notification);
            user.sendNotification(newNotification);
            userRepository.save(user);
        }
    }

    public void sendChallengeCreatedNotificationToAllCompanyFollowers(Challenge challenge) {
        Notification notification = new Notification();
        notification.setTitle("🌟Nieuwe challenge van " + challenge.getDepartment().getParentCompany().getName());
        notification.setDescription(challenge.department.parentCompany.getName() + " heeft een nieuwe challenge geplaatst!");
        notification.setLink("/challenge/" + challenge.getId());
        sendNotificationToAllFollowers(challenge.getDepartment().getParentCompany(), notification);
    }

    public void sendChallengeUpdatedNotificationToAllCompanyFollowers(Challenge challenge) {
        Notification notification = new Notification();
        notification.setTitle(challenge.getDepartment().getParentCompany().getName() + " heeft een challenge bijgewerkt");
        notification.setDescription("✒️De challenge \"" + challenge.getTitle() + "\" is bijgewerkt!");
        notification.setLink("/challenge/" + challenge.getId());
        sendNotificationToAllFollowers(challenge.getDepartment().getParentCompany(), notification);
    }

    public void sendUsersChosenChallengeNotification(ChallengeInput reaction){
        Notification notification = new Notification();
        notification.setTitle("📖Jouw idee is gekozen door " + reaction.getChallenge().getDepartment().getParentCompany().getName());
        notification.setDescription(reaction.getChallenge().getDepartment().getParentCompany().getName() + " heeft uw reactie verkozen als antwoord!");
        notification.setLink("/challenge/" + reaction.getChallenge().getId());
        sendNotificationToUser(reaction.getAuthor(), notification);
    }
}
