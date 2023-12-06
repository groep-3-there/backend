package matchmaker.backend.controllers;

import com.google.gson.Gson;
import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.constants.Perm;
import matchmaker.backend.models.Branch;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;

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

    @Autowired
    private BranchRepository branchRepository;

  @GetMapping("/graph-data/challenges/total")
  public ResponseEntity<Long> getTotalChallenges(
      @RequestAttribute("loggedInUser") User currentUser) {
    if (!currentUser.hasPermissionAtDepartment(
        Perm.COMPANY_GRADE, currentUser.department.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    return ResponseEntity.ok(challengeRepository.count());
  }

  @GetMapping("/graph-data/challenges/total-by-date")
  public ResponseEntity<LinkedHashMap<String, Long>> getTotalChallengesByDate(
      @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate from,
      @RequestParam(value = "till") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate till,
      @RequestAttribute("loggedInUser") User currentUser) {
    if (!currentUser.hasPermissionAtDepartment(
        Perm.COMPANY_GRADE, currentUser.department.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    LinkedHashMap<String, Long> json = new LinkedHashMap<>();

    long total = 0L;
    for (LocalDate date = from;
        date.isBefore(till) || date.isEqual(till);
        date = date.plusMonths(1)) {
      total += getChallengesByDateCount(date);
      json.put(date.getMonth().name() + "-" + date.getYear(), total);
    }
    return ResponseEntity.ok(json);
  }

  private Long getChallengesByDateCount(LocalDate date) {
    return challengeRepository.countByCreatedAtBetween(date, date.plusMonths(1));
  }

  @GetMapping("/graph-data/challenges/status")
  public ResponseEntity<LinkedHashMap<String, Long>> getChallengesByStatusCount(
      @RequestAttribute("loggedInUser") User currentUser) {
    if (!currentUser.hasPermissionAtDepartment(
        Perm.COMPANY_GRADE, currentUser.department.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    LinkedHashMap<String, Long> json = new LinkedHashMap<>();

    for (ChallengeStatus challengeStatus : ChallengeStatus.values()) {
      json.put(challengeStatus.name(), getChallengesByStatusCount(challengeStatus));
    }

    return ResponseEntity.ok(json);
  }

  private Long getChallengesByStatusCount(ChallengeStatus challengeStatus) {
    return challengeRepository.countByStatus(challengeStatus);
  }

    @GetMapping("/graph-data/challenges/filter/date")
    public ResponseEntity<LinkedHashMap<String, Long>> getChallengesForRangeOfMonthsFilter(
            @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate from,
            @RequestParam(value = "till") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate till,
            @RequestAttribute("loggedInUser") User currentUser) {
        if (!currentUser.hasPermissionAtDepartment(
                Perm.COMPANY_GRADE, currentUser.department.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        LinkedHashMap<String, Long> json = new LinkedHashMap<>();
        // For each month between from and till, get the amount of challenges
        for (LocalDate date = from;
             date.isBefore(till) || date.isEqual(till);
             date = date.plusMonths(1)) {
            json.put(
                    date.getMonth().name() + "-" + date.getYear(),
                    challengeRepository.countByCreatedAtBetween(date, date.plusMonths(1)));
        }
        return ResponseEntity.ok(json);
    }

    @GetMapping("/graph-data/users/total")
    public ResponseEntity<Long> getTotalUsers(@RequestAttribute("loggedInUser") User currentUser) {
        if (!currentUser.hasPermissionAtDepartment(
                Perm.COMPANY_GRADE, currentUser.department.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(userRepository.count());
    }

    @GetMapping("/graph-data/users/total-by-date")
    public ResponseEntity<LinkedHashMap<String, Long>> getTotalUsersByDate(
            @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate from,
            @RequestParam(value = "till") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate till,
            @RequestAttribute("loggedInUser") User currentUser) {
        if (!currentUser.hasPermissionAtDepartment(
                Perm.COMPANY_GRADE, currentUser.department.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }


        LinkedHashMap<String, Long> json = new LinkedHashMap<>();

        long total = 0L;
        for (LocalDate date = from;
             date.isBefore(till) || date.isEqual(till);
             date = date.plusMonths(1)) {
            total += getUsersByDateCount(date);
            json.put(
                    date.getMonth().name() + "-" + date.getYear(),
                    total
            );
        }
        return ResponseEntity.ok(json);
    }

    private Long getUsersByDateCount(LocalDate date) {
        return userRepository.countByCreatedAtBetween(date, date.plusMonths(1));
    }

    @GetMapping("/graph-data/companies/total")
    public ResponseEntity<Long> getTotalCompanies(
            @RequestAttribute("loggedInUser") User currentUser) {
        if (!currentUser.hasPermissionAtDepartment(
                Perm.COMPANY_GRADE, currentUser.department.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(companyRepository.count());
    }

    @GetMapping("/graph-data/companies/total-by-date")
    public ResponseEntity<LinkedHashMap<String, Long>> getTotalCompaniesByDate(
            @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate from,
            @RequestParam(value = "till") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate till,
            @RequestAttribute("loggedInUser") User currentUser)
    {
        if (!currentUser.hasPermissionAtDepartment(
                Perm.COMPANY_GRADE, currentUser.department.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        LinkedHashMap<String, Long> json = new LinkedHashMap<>();

        long total = 0L;
        for (LocalDate date = from;
             date.isBefore(till) || date.isEqual(till);
             date = date.plusMonths(1)) {
            total += getCompaniesByDateCount(date);
            json.put(
                    date.getMonth().name() + "-" + date.getYear(),
                    total
            );
        }
        return ResponseEntity.ok(json);
    }

    private Long getCompaniesByDateCount(LocalDate date) {
        return companyRepository.countByCreatedAtBetween(date, date.plusMonths(1));
    }

    @GetMapping("/graph-data/companies/{companyId}/challenges/total")
    public ResponseEntity<Long> getTotalChallengesForCompany(
            @PathVariable("companyId") Long companyId,
            @RequestAttribute("loggedInUser") User currentUser)
    {
        if (!currentUser.hasPermissionAtDepartment(
                Perm.COMPANY_MANAGE, currentUser.department.getId())) {
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
            @RequestAttribute("loggedInUser") User currentUser)
    {
        if (!currentUser.hasPermissionAtDepartment(
                Perm.COMPANY_MANAGE, currentUser.department.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(companyRequestRepository.count());
    }

    @GetMapping("/graph-data/companies/filter/date")
    public ResponseEntity<LinkedHashMap<String, Long>> getCompaniesForRangeOfMonthsFilter(
            @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate from,
            @RequestParam(value = "till") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate till,
            @RequestAttribute("loggedInUser") User currentUser)
    {
        if (!currentUser.hasPermissionAtDepartment(
                Perm.COMPANY_GRADE, currentUser.department.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        LinkedHashMap<String, Long> json = new LinkedHashMap<>();
        // For each month between from and till, get the amount of challenges
        for (LocalDate date = from;
             date.isBefore(till) || date.isEqual(till);
             date = date.plusMonths(1)) {
            json.put(
                    date.getMonth().name() + "-" + date.getYear(),
                    companyRepository.countByCreatedAtBetween(date, date.plusMonths(1)));
        }
        return ResponseEntity.ok(json);
    }

    @GetMapping("/graph-data/challenge-inputs/filter/date")
    public ResponseEntity<LinkedHashMap<String, Long>> getChallengeInputsForRangeOfMonthsFilter(
            @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate from,
            @RequestParam(value = "till") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate till,
            @RequestAttribute("loggedInUser") User currentUser)
    {
        if (!currentUser.hasPermissionAtDepartment(
                Perm.COMPANY_GRADE, currentUser.department.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        LinkedHashMap<String, Long> json = new LinkedHashMap<>();
        // For each month between from and till, get the amount of challenges
        for (LocalDate date = from;
             date.isBefore(till) || date.isEqual(till);
             date = date.plusMonths(1)) {
            json.put(
                    date.getYear() + " " + date.getMonth().name(),
                    challengeInputRepository.countByCreatedAtBetween(date, date.plusMonths(1)));
        }
        return ResponseEntity.ok(json);
    }

    @GetMapping("/graph-data/companies/total-by-branch")
    public ResponseEntity<LinkedHashMap<String, Long>> getTotalCompaniesByBranch(
            @RequestAttribute("loggedInUser") User currentUser)
    {
        if (!currentUser.hasPermissionAtDepartment(
                Perm.COMPANY_GRADE, currentUser.department.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        LinkedHashMap<String, Long> json = new LinkedHashMap<>();

        Iterable<Branch> branches = branchRepository.findAll();

        for (Branch branch : branches) {
            json.put(
                    branch.getName(),
                    companyRepository.countByBranch(branch)
            );
        }

        return ResponseEntity.ok(json);
    }
}
