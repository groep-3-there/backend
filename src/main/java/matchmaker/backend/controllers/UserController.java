package matchmaker.backend.controllers;

import matchmaker.backend.models.User;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;



    @GetMapping("/user/{id}")
    public Optional<User> getUserById(@PathVariable("id") Long id){
        return Optional.of(userRepository.findById(id).get());
    }

}
