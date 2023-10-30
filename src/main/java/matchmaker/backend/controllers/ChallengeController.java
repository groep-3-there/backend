package matchmaker.backend.controllers;

import matchmaker.backend.models.Challenge;
import matchmaker.backend.models.Image;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.ChallengeRepository;
import matchmaker.backend.repositories.ImageRepository;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
public class ChallengeController {

    @Autowired
    private ChallengeRepository repository;

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/challenges")
    public Iterable<Challenge> getChallenges() {
        return repository.findAll();
    }

    @GetMapping("/challenges/{userid}")
    public Iterable<Challenge> getChallengesByUUID(@PathVariable("userid") Long userid) {
        return repository.findByAuthorId(userid);
    }
}
