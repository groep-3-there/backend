package matchmaker.backend.controllers;

import matchmaker.backend.models.User;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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