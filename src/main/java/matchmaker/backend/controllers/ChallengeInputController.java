package matchmaker.backend.controllers;

import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.models.Challenge;
import matchmaker.backend.models.ChallengeInput;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.ChallengeRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import matchmaker.backend.repositories.ChallengeInputRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ChallengeInputController {

    @Autowired
    private ChallengeInputRepository repository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @GetMapping("/reaction/challenge/{id}")
    public ResponseEntity<Iterable<ChallengeInput>> getChallengeReactions(@PathVariable("id") Long id) {
        List<ChallengeInput> reactions = repository.findAllByChallengeId(id);
        return new ResponseEntity<>(reactions, HttpStatus.OK);
    }

    @GetMapping("/reaction/{id}")
    public ResponseEntity getChallengeReactionById(@PathVariable("id") Long id) {

        if(!repository.existsById(id)){return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}

        return ResponseEntity.status(HttpStatus.OK).body(repository.findById(id).get());

    }

    @PostMapping("/reaction/create/{id}")
    public ResponseEntity createReactionOnChallenge(@RequestBody ChallengeInput reaction,
                                                    @RequestAttribute("loggedInUser") User currentUser,
                                                    @PathVariable("id") Long challengeId){
        //Check if the user is logged in
        if(currentUser == null){ return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); }

        //Check if the challenge exists
        Optional<Challenge> challenge = challengeRepository.findById(challengeId);
        if(challenge.isEmpty()){ return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); }

        Challenge filledChallenge = challenge.get();

        //Check if the challenge is open for ideas
        if(filledChallenge.status != ChallengeStatus.OPEN_VOOR_IDEEEN){ return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null); }

        //If the user cant see the challenge, it should not be able to react to it.
        if(!filledChallenge.canBeSeenBy(currentUser)){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);}

        //The user can see the challenge, and the challenge is open for ideas, so we can save the reaction.
        reaction.author = currentUser;
        reaction.challenge = filledChallenge;
        repository.save(reaction);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}