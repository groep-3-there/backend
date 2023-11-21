package matchmaker.backend.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import matchmaker.backend.models.Image;
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

  @Autowired private UserRepository userRepository;

  @Autowired private ImageRepository imageRepository;

    private static final Pattern EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern PHONENUMBER = Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$");

    @Autowired
    private FirebaseAuth firebaseAuth;


    @GetMapping("/user/{id}")
    public ResponseEntity<Optional<User>> getUserById(
            @PathVariable("id") Long id,
            @RequestAttribute(name = "loggedInUser", required = false) User currentUser) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Optional.empty());
        }
        //checks if the current user can see the profile's email and phone number
        if (currentUser == null || !currentUser.id.equals(user.get().id)) {
            if (!user.get().isEmailPublic) {
                user.get().email = null;
            }
            if (!user.get().isPhoneNumberPublic) {
                user.get().phoneNumber = null;
            }
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

        //get the current avatar image if there is one
        Optional<Image> image = Optional.empty();
        if (user.avatarImageId != null){
            image = imageRepository.findById(user.avatarImageId);
        }

        //check if the image the user wants is in the database
        if (image.isEmpty() && user.avatarImageId != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        //set the avatar image
        checkedUser.setAvatarImageId(user.avatarImageId);

        //set public information
        checkedUser.setEmailPublic(user.isEmailPublic);
        checkedUser.setPhoneNumberPublic(user.isPhoneNumberPublic);

        //save the user to the database
        User saveUser = userRepository.save(checkedUser);

        return ResponseEntity.status(HttpStatus.OK).body(saveUser);
    }

  @GetMapping("/user/exist/{email}")
  public ResponseEntity<Boolean> checkIfUserExists(@PathVariable("email") String email) {
    return ResponseEntity.ok(userRepository.findByEmail(email).isPresent());
  }
}
