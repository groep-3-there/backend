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

    public void sendNotificationToUser(User user, Notification notification) {
        Notification newNotification = new Notification(notification);
        if(user == null){
            return;
        }
        user.sendNotification(newNotification);
        userRepository.save(user);
        //Don't send email to test users
        if( user.email == null || user.email.contains("@email")){
            return;
        }

        try {
            emailService.sendEmail(user.getEmail(), user.getName(), notification.getTitle(), notification.getDescription(), "Bekijk", "https://matchmakergroep3.nl" + notification.getLink());
        } catch (MailjetException e) {
            e.printStackTrace();
        }
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
        notification.setTitle("✨Nieuwe challenge van " + challenge.getDepartment().getParentCompany().getName());
        notification.setDescription(challenge.department.parentCompany.getName() + " heeft een nieuwe challenge geplaatst!");
        notification.setLink("/challenge/" + challenge.getId());
        for (Long follower : challenge.getDepartment().getParentCompany().getFollowerIds()) {
            User user = userRepository.findById(follower).get();
            Notification newNotificationForUser = new Notification(notification);
            //Only send notification to users who are allowed see the challenge
            if(challenge.canBeSeenBy(user)){
                sendNotificationToUser(user, newNotificationForUser);
            }
        }
    }

    public void sendChallengeUpdatedNotificationToAllCompanyFollowers(Challenge challenge) {
        Notification notification = new Notification();
        notification.setTitle(challenge.getDepartment().getParentCompany().getName() + " heeft een challenge bijgewerkt");
        notification.setDescription("✒️De challenge \"" + challenge.getTitle() + "\" is bijgewerkt!");
        notification.setLink("/challenge/" + challenge.getId());
        for (Long follower : challenge.getDepartment().getParentCompany().getFollowerIds()) {
            User user = userRepository.findById(follower).get();
            Notification newNotificationForUser = new Notification(notification);
            //Only send notification to users who are allowed see the challenge
            if(challenge.canBeSeenBy(user)){
                sendNotificationToUser(user, newNotificationForUser);
            }
        }
    }

    public void sendUsersChosenChallengeNotification(ChallengeInput reaction){
        Notification notification = new Notification();
        notification.setTitle("📖Jouw idee is gekozen door " + reaction.getChallenge().getDepartment().getParentCompany().getName());
        notification.setDescription(reaction.getChallenge().getDepartment().getParentCompany().getName() + " heeft uw reactie verkozen als antwoord!");
        notification.setLink("/challenge/" + reaction.getChallenge().getId());
        sendNotificationToUser(reaction.getAuthor(), notification);
    }

    public void sendAuthorNewReactionNotification(ChallengeInput reaction){
        Notification notification = new Notification();
        notification.setTitle("✨Nieuwe reactie op jouw challenge");
        notification.setDescription(reaction.getAuthor().getName() + " heeft gereageerd op jouw challenge!");
        notification.setLink("/challenge/" + reaction.getChallenge().getId());
        sendNotificationToUser(reaction.getChallenge().getAuthor(), notification);
    }

    public void sendAccountCreatedNotification(User user){
        Notification notification = new Notification();
        notification.setTitle("✨Welkom bij het MatchMaker platform!");
        notification.setDescription("Wij zijn blij dat u gekozen heeft voor MatchMaker, heeft u al een leuke challenge gevonden?");
        notification.setLink("/challenges");
        sendNotificationToUser(user, notification);
    }
}
