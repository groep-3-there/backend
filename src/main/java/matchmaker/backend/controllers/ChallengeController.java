package matchmaker.backend.controllers;

import matchmaker.backend.models.Challenge;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.ChallengeRepository;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ChallengeController {
    @Autowired
    private ChallengeRepository repository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/challenges")
    public Iterable<Challenge> getChallenges() {
        return repository.findAll();
    }

    @GetMapping("/challenges/{userid}")
    public Iterable<Challenge> getChallengesByUUID(@PathVariable("userid") Long userid) {
        return repository.findByAuthorId(userid);
    }
}
