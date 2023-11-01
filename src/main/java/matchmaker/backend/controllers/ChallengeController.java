package matchmaker.backend.controllers;

import matchmaker.backend.models.Challenge;
import matchmaker.backend.models.Image;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.ChallengeRepository;
import matchmaker.backend.repositories.ImageRepository;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ChallengeController {

    @Autowired
    private ChallengeRepository repository;

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/challenge")
    public Iterable<Challenge> getChallenges() {
        return repository.findAll();
    }

    @GetMapping("/challenge/{id}")
    public Optional<Challenge> getChallengeById(@PathVariable("id")Long id) {
     return repository.findById(id);
    }

    @PostMapping(path = "/challenge")
    public Optional<Challenge> createChallenge(@RequestBody Challenge newChallenge) {
        Challenge checkedChallenge = new Challenge();

        //Only copy values we trust from the enduser. If user passes id, it is ignored.
        checkedChallenge.contactInformation = newChallenge.contactInformation;
        checkedChallenge.title = newChallenge.title;
        checkedChallenge.description = newChallenge.description;
        checkedChallenge.banner = newChallenge.banner;
        checkedChallenge.concludingRemarks = newChallenge.concludingRemarks;
        checkedChallenge.summary = newChallenge.summary;
        checkedChallenge.status = newChallenge.status;
        checkedChallenge.endDate = newChallenge.endDate;
        checkedChallenge.tags = newChallenge.tags;
        checkedChallenge.branch = newChallenge.branch;
        checkedChallenge.isPublicVisible = newChallenge.isPublicReactable;
        checkedChallenge.isPublicReactable = newChallenge.isPublicReactable;

        Challenge savedChallenge = repository.save(checkedChallenge);
        return Optional.of(savedChallenge);
    }

}
