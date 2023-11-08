package matchmaker.backend.IntegrationTests;

import matchmaker.backend.UnitTests.User;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/auth/user")
    public User getLoggedInUser(@RequestAttribute("loggedInUser") User currentUser){
        //Hardcoded to user 1 for now
        //TODO: make this depend on the session from firebase.
        return currentUser;
    }

}