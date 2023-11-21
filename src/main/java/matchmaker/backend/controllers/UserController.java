package matchmaker.backend.controllers;

import matchmaker.backend.models.User;
import matchmaker.backend.repositories.ImageRepository;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

  @Autowired private UserRepository userRepository;

  @Autowired private ImageRepository imageRepository;

  @GetMapping("/user/{id}")
  public ResponseEntity<User> getUserById(
      @PathVariable("id") Long id,
      @RequestAttribute(name = "loggedInUser", required = false) User currentUser) {
    Optional<User> user = userRepository.findById(id);
    if (user.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    User appliedPrivacy = user.get().viewAs(currentUser);
    return ResponseEntity.status(200).body(appliedPrivacy);
  }

  @GetMapping("/user/exist/{email}")
  public ResponseEntity<Boolean> checkIfUserExists(@PathVariable("email") String email) {
    return ResponseEntity.ok(userRepository.findByEmail(email).isPresent());
  }
}
