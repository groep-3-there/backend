package matchmaker.backend.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.mailjet.client.errors.MailjetException;
import matchmaker.backend.EmailService;
import matchmaker.backend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class RootController {

  @Autowired private FirebaseAuth firebaseAuth;
  @Autowired private EmailService emailService;
  @GetMapping("/ping")
  public HashMap<String, String> ping() {
    HashMap<String, String> map = new HashMap<>();
    map.put("ping", "pong");
    return map;
  }
  @GetMapping("/mailme")
  public String sendEmail(@RequestAttribute(name = "loggedInUser", required = false) User currentUser) throws MailjetException {
    if(currentUser == null) return "You are not logged in";
    try {
      emailService.sendEmail(currentUser.getEmail(), currentUser.getName(), "Test email", "Dit is een test email", "Klik", "https://matchmakergroep3.nl/");
    } catch (Exception e) {
      throw e;
    }
    return "verzonden";
  }

  @GetMapping(path = "/whoami")
  public HashMap<String, String> test(
      @RequestAttribute(name = "loggedInUser", required = false) User currentUser)
      throws FirebaseAuthException {
    HashMap<String, String> map = new HashMap<>();

    if (currentUser == null) {
      map.put("account", "no");
      return map;
    }
    map.put("firebase uid", firebaseAuth.getUser(currentUser.firebaseId).getUid());
    map.put("firebase email", firebaseAuth.getUser(currentUser.firebaseId).getEmail());
    return map;
  }
}
