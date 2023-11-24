package matchmaker.backend.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import matchmaker.backend.RequestBodies.CreateUserFields;
import matchmaker.backend.constants.DefaultRoleId;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.DepartmentCodeRepository;
import matchmaker.backend.repositories.RoleRepository;
import matchmaker.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class AuthController {

  @Autowired private UserRepository userRepository;

  @Autowired private FirebaseAuth firebaseAuth;
  @Autowired private DepartmentCodeRepository departmentCodeRepository;

  private Logger log = LoggerFactory.getLogger(AuthController.class);
  @Autowired private RoleRepository roleRepository;

  @GetMapping("/auth/user")
  public User getLoggedInUser(
      @RequestAttribute(name = "loggedInUser", required = false) User currentUser) {
    return currentUser;
  }

  @PostMapping("/auth/create")
  public ResponseEntity<User> createNewUser(@RequestBody CreateUserFields createUser) {

    if (userRepository.findByEmail(createUser.email).isPresent()) {
      return ResponseEntity.badRequest().body(null);
    }
    User checked = new User();
    checked.name = createUser.name;
    checked.email = createUser.email;
    checked.createdAt = new Date();
    checked.isEmailPublic = false;
    checked.isPhoneNumberPublic = false;
    checked.info = "";
    checked.tags = "";
    checked.acceptedTosDate = new Date();

    if (createUser.companyCode != null) {
      departmentCodeRepository.findByCode(createUser.companyCode);
      if (departmentCodeRepository.findByCode(createUser.companyCode).isEmpty()) {
        log.info("Department code niet geldig");
      } else {
        checked.department =
            departmentCodeRepository.findByCode(createUser.companyCode).get().department;
        checked.role = roleRepository.findById(DefaultRoleId.MEDEWERKER).get();
      }
    }

    try {
      UserRecord createReq =
          firebaseAuth.createUser(
              new UserRecord.CreateRequest()
                  .setEmail(checked.email)
                  .setPassword(createUser.password));
      checked.firebaseId = createReq.getUid();

      User saved = userRepository.save(checked);
      return ResponseEntity.ok().body(saved);
    } catch (FirebaseAuthException e) {
      throw new RuntimeException(e);
    }
  }
}
