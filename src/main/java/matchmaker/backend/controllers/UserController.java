package matchmaker.backend.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.ImageRepository;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.regex.Pattern;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    private static final Pattern EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern PHONENUMBER = Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$");

    @Autowired
    private FirebaseAuth firebaseAuth;


    @GetMapping("/user/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable("id") Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Optional.empty());
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> UpdateUserProfile(
            @PathVariable("id") Long id,
            @RequestBody User user,
            @RequestAttribute(name = "loggedInUser", required = false) User currentUser) {
        //check if the user can edit the profile
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!currentUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<User> targetUser = userRepository.findById(id);

        if (targetUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User checkedUser = targetUser.get();

        //check if the name is not blank or null
        if (user.getName() == null || user.getName().isBlank())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        checkedUser.setName(user.name);

        //check if the email is not blank or null or valid
        if (user.getEmail() == null || user.getEmail().isBlank() || !EMAIL.matcher(user.getEmail()).matches())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        checkedUser.setEmail(user.email);

        //check if the phoneNumber is not blank or null or valid
        if (user.getPhoneNumber() == null || !PHONENUMBER.matcher(user.getPhoneNumber()).matches())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        checkedUser.setPhoneNumber(user.phoneNumber);

        checkedUser.setInfo(user.info);

        if (user.tags.endsWith(",")) {
            String tags = user.tags;
            user.tags = tags.substring(0, tags.length() - 1);
        }
        checkedUser.setTags(user.tags);

        //set public information
        checkedUser.setEmailPublic(user.isEmailPublic);
        checkedUser.setPhoneNumberPublic(user.isPhoneNumberPublic);

        //save the user to the database
        User saveUser = userRepository.save(checkedUser);

        return ResponseEntity.status(HttpStatus.OK).body(saveUser);
    }
}
