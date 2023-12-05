package matchmaker.backend.controllers;

import com.google.gson.Gson;
import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.constants.Perm;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;

@RestController
public class GraphDataController {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyRequestRepository companyRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChallengeInputRepository challengeInputRepository;

    @GetMapping("/graph-data/challenges/total")
    public ResponseEntity<Long> getTotalChallenges(
            @RequestAttribute("loggedInUser") User currentUser
    ) {
        if (!currentUser.hasPermissionAtDepartment(Perm.COMPANY_GRADE, currentUser.department.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(challengeRepository.count());
    }

    @GetMapping("/graph-data/challenges/{status}")
    public ResponseEntity<Long> getChallengesByStatusCount(
            @PathVariable("status") ChallengeStatus status,
            @RequestAttribute("loggedInUser") User currentUser
    ) {
        if (!currentUser.hasPermissionAtDepartment(Perm.COMPANY_GRADE, currentUser.department.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(challengeRepository.countByStatus(status));
    }

    @GetMapping("/graph-data/challenges/filter/date")
    public ResponseEntity<String> getChallengesForRangeOfMonthsFilter(
            @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate from,
            @RequestParam(value = "till") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate till,
            @RequestAttribute("loggedInUser") User currentUser)
    {
        if (!currentUser.hasPermissionAtDepartment(Perm.COMPANY_GRADE, currentUser.department.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        HashMap<String, Long> json = new HashMap<>();
        // For each month between from and till, get the amount of challenges
        for (LocalDate date = from;
             date.isBefore(till) || date.isEqual(till);
             date = date.plusMonths(1)) {
            json.put(
                    date.getMonth().name(),
                    challengeRepository.countByCreatedAtBetween(date, date.plusMonths(1)));
        }
        return ResponseEntity.ok(new Gson().toJson(json));
    }

    @GetMapping("/graph-data/users/total")
    public ResponseEntity<Long> getTotalUsers(
            @RequestAttribute("loggedInUser") User currentUser
    ) {
        if (!currentUser.hasPermissionAtDepartment(Perm.COMPANY_GRADE, currentUser.department.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(userRepository.count());
    }

    @GetMapping("/graph-data/companies/total")
    public ResponseEntity<Long> getTotalCompanies(
            @RequestAttribute("loggedInUser") User currentUser
    ) {
        if (!currentUser.hasPermissionAtDepartment(Perm.COMPANY_GRADE, currentUser.department.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(companyRepository.count());
    }

    @GetMapping("/graph-data/companies/{companyId}/challenges/total")
    public ResponseEntity<Long> getTotalChallengesForCompany(
            @PathVariable("companyId") Long companyId,
            @RequestAttribute("loggedInUser") User currentUser)
    {
        if (!currentUser.hasPermissionAtDepartment(Perm.COMPANY_MANAGE, currentUser.department.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(
                challengeRepository
                        .findChallengesByDepartment_ParentCompanyId(companyId)
                        .spliterator()
                        .getExactSizeIfKnown());
    }

    @GetMapping("/graph-data/company-requests/total")
    public ResponseEntity<Long> getTotalCompanyRequests(
            @RequestAttribute("loggedInUser") User currentUser
    ) {
        if (!currentUser.hasPermissionAtDepartment(Perm.COMPANY_MANAGE, currentUser.department.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(companyRequestRepository.count());
    }

    @GetMapping("/graph-data/companies/filter/date")
    public ResponseEntity<String> getCompaniesForRangeOfMonthsFilter(
            @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate from,
            @RequestParam(value = "till") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate till,
            @RequestAttribute("loggedInUser") User currentUser)
    {
        if (!currentUser.hasPermissionAtDepartment(Perm.COMPANY_GRADE, currentUser.department.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        HashMap<String, Long> json = new HashMap<>();

        for (LocalDate date = from;
             date.isBefore(till) || date.isEqual(till);
             date = date.plusMonths(1)) {
            json.put(
                    date.getMonth().name(),
                    companyRepository.countByCreatedAtBetween(date, date.plusMonths(1)));
        }

        return ResponseEntity.ok(new Gson().toJson(json));
    }

    @GetMapping("/graph-data/challenge-inputs/filter/date")
    public ResponseEntity<String> getChallengeInputsForRangeOfMonthsFilter(
            @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate from,
            @RequestParam(value = "till") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate till) {
        HashMap<String, Long> json = new HashMap<>();

        for (LocalDate date = from;
             date.isBefore(till) || date.isEqual(till);
             date = date.plusMonths(1)) {
            json.put(
                    date.getMonth().name(),
                    challengeInputRepository.countByCreatedAtBetween(date, date.plusMonths(1)));
        }

        return ResponseEntity.ok(new Gson().toJson(json));
    }
}
