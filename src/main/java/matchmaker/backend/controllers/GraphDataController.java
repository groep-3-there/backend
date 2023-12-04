package matchmaker.backend.controllers;

import com.google.gson.Gson;
import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.models.Challenge;
import matchmaker.backend.repositories.ChallengeRepository;
import matchmaker.backend.repositories.CompanyRepository;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class GraphDataController {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/graph-data/challenges/total")
    public ResponseEntity<Long> getTotalChallenges() {
        return ResponseEntity.ok(challengeRepository.count());
    }

    @GetMapping("/graph-data/challenges/{status}")
    public ResponseEntity<Long> getChallengesByStatusCount(@PathVariable("status") ChallengeStatus status) {
        return ResponseEntity.ok(challengeRepository.countByStatus(status));
    }

    @GetMapping("/graph-data/challenge/filter/date")
    public ResponseEntity<String> getChallengesForRangeOfMonthsFilter(
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy.MM.dd")
            LocalDate from,
            @RequestParam(value = "till", required = false) @DateTimeFormat(pattern = "yyyy.MM.dd")
            LocalDate till) {
        HashMap<String, Long> json = new HashMap();
        //For each month between from and till, get the amount of challenges
        for (LocalDate date = from; date.isBefore(till) || date.isEqual(till); date = date.plusMonths(1)) {
            json.put(date.getMonth().name(), getChallengesByMonth(date));
        }
        return  ResponseEntity.ok(new Gson().toJson(json));
    }

    public long getChallengesByMonth(LocalDate date){
        Iterable<Challenge> allChallenges = challengeRepository.findAll();
        //Filter all on the given date
        List<Challenge> filteredChallenges = new ArrayList<>();
        for (Challenge challenge : allChallenges) {
            if (challenge.createdAt.getMonth().equals(date.getMonth()) && challenge.createdAt.getYear() == date.getYear()) {
                filteredChallenges.add(challenge);
            }
        }
        return filteredChallenges.size();
    }

    @GetMapping("/graph-data/users/total")
    public ResponseEntity<Long> getTotalUsers() {
        return ResponseEntity.ok(userRepository.count());
    }

    @GetMapping("/graph-data/companies/total")
    public ResponseEntity<Long> getTotalCompanies() {
        return ResponseEntity.ok(companyRepository.count());
    }



}
