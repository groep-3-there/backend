package matchmaker.backend.controllers;

import matchmaker.backend.models.User;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/users/{userid}")
    public User getRandomUser(@PathVariable("userid") Long userid) {
        return userRepository.findById(userid).get();
    }
}
