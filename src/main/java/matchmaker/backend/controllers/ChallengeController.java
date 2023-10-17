package matchmaker.backend.controllers;

import matchmaker.backend.models.Challenge;
import matchmaker.backend.repositories.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ChallengeController {
    @Autowired
    private ChallengeRepository repository;

    @GetMapping("/challenges")
    public Iterable<Challenge> getChallenges() {
        return repository.findAll();
    }

    @GetMapping("/challenges/{uuid}")
    public Iterable<Challenge> getChallengesByUUID(@PathVariable("uuid") UUID uuid) {
        return repository.findByAuthorId(uuid);
    }
}
