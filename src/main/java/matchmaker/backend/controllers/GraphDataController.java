package matchmaker.backend.controllers;

import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.constants.Perm;
import matchmaker.backend.models.Branch;
import matchmaker.backend.models.Department;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Objects;

@RestController
public class GraphDataController {

  @Autowired private ChallengeRepository challengeRepository;

  @Autowired private CompanyRepository companyRepository;

  @Autowired private CompanyRequestRepository companyRequestRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private ChallengeInputRepository challengeInputRepository;

  @Autowired private BranchRepository branchRepository;

  @Autowired private DepartmentRepository departmentRepository;

  @GetMapping("/graph-data/challenges/total")
  public ResponseEntity<Long> getTotalChallenges(
      @RequestAttribute("loggedInUser") User currentUser) {
    if (!currentUser.hasPermissionAtDepartment(
        Perm.MATCHMAKER_GRAPH_READ, currentUser.department.getId())) {
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
        Perm.MATCHMAKER_GRAPH_READ, currentUser.department.getId())) {
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
    return challengeRepository.countByCreatedAtBetween(date, date.plusMonths(1).minusDays(1));
  }

  @GetMapping("/graph-data/challenges/status")
  public ResponseEntity<LinkedHashMap<String, Long>> getChallengesByStatusCount(
      @RequestAttribute("loggedInUser") User currentUser) {
    if (!currentUser.hasPermissionAtDepartment(
        Perm.MATCHMAKER_GRAPH_READ, currentUser.department.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    LinkedHashMap<String, Long> json = new LinkedHashMap<>();

    for (ChallengeStatus challengeStatus : ChallengeStatus.values()) {
      json.put(challengeStatus.name(), challengeRepository.countByStatus(challengeStatus));
    }

    return ResponseEntity.ok(json);
  }

  @GetMapping("/graph-data/challenges/filter/date")
  public ResponseEntity<LinkedHashMap<String, Long>> getChallengesForRangeOfMonthsFilter(
      @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate from,
      @RequestParam(value = "till") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate till,
      @RequestAttribute("loggedInUser") User currentUser) {
    if (!currentUser.hasPermissionAtDepartment(
        Perm.MATCHMAKER_GRAPH_READ, currentUser.department.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    LinkedHashMap<String, Long> json = new LinkedHashMap<>();
    // For each month between from and till, get the amount of challenges
    for (LocalDate date = from;
        date.isBefore(till) || date.isEqual(till);
        date = date.plusMonths(1)) {
      json.put(
          date.getMonth().name() + "-" + date.getYear(),
          challengeRepository.countByCreatedAtBetween(date, date.plusMonths(1).minusDays(1)));
    }
    return ResponseEntity.ok(json);
  }

  @GetMapping("/graph-data/users/total")
  public ResponseEntity<Long> getTotalUsers(@RequestAttribute("loggedInUser") User currentUser) {
    if (!currentUser.hasPermissionAtDepartment(
        Perm.MATCHMAKER_GRAPH_READ, currentUser.department.getId())) {
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
        Perm.MATCHMAKER_GRAPH_READ, currentUser.department.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    LinkedHashMap<String, Long> json = new LinkedHashMap<>();

    long total = 0L;
    for (LocalDate date = from;
        date.isBefore(till) || date.isEqual(till);
        date = date.plusMonths(1)) {
      total += getUsersByDateCount(date);
      json.put(date.getMonth().name() + "-" + date.getYear(), total);
    }
    return ResponseEntity.ok(json);
  }

  private Long getUsersByDateCount(LocalDate date) {
    return userRepository.countByCreatedAtBetween(date, date.plusMonths(1).minusDays(1));
  }

  @GetMapping("/graph-data/companies/total")
  public ResponseEntity<Long> getTotalCompanies(
      @RequestAttribute("loggedInUser") User currentUser) {
    if (!currentUser.hasPermissionAtDepartment(
        Perm.MATCHMAKER_GRAPH_READ, currentUser.department.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    return ResponseEntity.ok(companyRepository.count());
  }

  @GetMapping("/graph-data/companies/total-by-date")
  public ResponseEntity<LinkedHashMap<String, Long>> getTotalCompaniesByDate(
      @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate from,
      @RequestParam(value = "till") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate till,
      @RequestAttribute("loggedInUser") User currentUser) {
    if (!currentUser.hasPermissionAtDepartment(
        Perm.MATCHMAKER_GRAPH_READ, currentUser.department.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    LinkedHashMap<String, Long> json = new LinkedHashMap<>();

    long total = 0L;
    for (LocalDate date = from;
        date.isBefore(till) || date.isEqual(till);
        date = date.plusMonths(1)) {
      total += getCompaniesByDateCount(date);
      json.put(date.getMonth().name() + "-" + date.getYear(), total);
    }
    return ResponseEntity.ok(json);
  }

  private Long getCompaniesByDateCount(LocalDate date) {
    return companyRepository.countByCreatedAtBetween(date, date.plusMonths(1).minusDays(1));
  }

  @GetMapping("/graph-data/company-requests/total")
  public ResponseEntity<Long> getTotalCompanyRequests(
      @RequestAttribute("loggedInUser") User currentUser) {
    if (!currentUser.hasPermissionAtDepartment(
        Perm.MATCHMAKER_GRAPH_READ, currentUser.department.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    return ResponseEntity.ok(companyRequestRepository.count());
  }

  @GetMapping("/graph-data/companies/filter/date")
  public ResponseEntity<LinkedHashMap<String, Long>> getCompaniesForRangeOfMonthsFilter(
      @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate from,
      @RequestParam(value = "till") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate till,
      @RequestAttribute("loggedInUser") User currentUser) {
    if (!currentUser.hasPermissionAtDepartment(
        Perm.MATCHMAKER_GRAPH_READ, currentUser.department.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    LinkedHashMap<String, Long> json = new LinkedHashMap<>();
    // For each month between from and till, get the amount of challenges
    for (LocalDate date = from;
        date.isBefore(till) || date.isEqual(till);
        date = date.plusMonths(1)) {
      json.put(
          date.getMonth().name() + "-" + date.getYear(),
          companyRepository.countByCreatedAtBetween(date, date.plusMonths(1).minusDays(1)));
    }
    return ResponseEntity.ok(json);
  }

  @GetMapping("/graph-data/challenge-inputs/filter/date")
  public ResponseEntity<LinkedHashMap<String, Long>> getChallengeInputsForRangeOfMonthsFilter(
      @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate from,
      @RequestParam(value = "till") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate till,
      @RequestAttribute("loggedInUser") User currentUser) {
    if (!currentUser.hasPermissionAtDepartment(
        Perm.MATCHMAKER_GRAPH_READ, currentUser.department.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    LinkedHashMap<String, Long> json = new LinkedHashMap<>();
    // For each month between from and till, get the amount of challenges
    for (LocalDate date = from;
        date.isBefore(till) || date.isEqual(till);
        date = date.plusMonths(1)) {
      json.put(
          date.getYear() + " " + date.getMonth().name(),
          challengeInputRepository.countByCreatedAtBetween(date, date.plusMonths(1).minusDays(1)));
    }
    return ResponseEntity.ok(json);
  }

  @GetMapping("/graph-data/companies/total-by-branch")
  public ResponseEntity<LinkedHashMap<String, Long>> getTotalCompaniesByBranch(
      @RequestAttribute("loggedInUser") User currentUser) {
    if (!currentUser.hasPermissionAtDepartment(
        Perm.MATCHMAKER_GRAPH_READ, currentUser.department.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    LinkedHashMap<String, Long> json = new LinkedHashMap<>();

    Iterable<Branch> branches = branchRepository.findAll();

    for (Branch branch : branches) {
      json.put(branch.getName(), companyRepository.countByBranch(branch));
    }

    return ResponseEntity.ok(json);
  }

  @GetMapping("/graph-data/company/{companyId}/challenges/filter/date")
  public ResponseEntity<LinkedHashMap<String, Long>> getChallengesForRangeOfMonthsFilterAndCompanyId(
          @PathVariable("companyId") Long companyId,
          @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate from,
          @RequestParam(value = "till") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate till,
          @RequestAttribute("loggedInUser") User currentUser) {
    if (!currentUser.hasPermissionAtDepartment(
            Perm.COMPANY_GRAPH_READ, currentUser.department.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    if(!Objects.equals(currentUser.department.parentCompany.id, companyId)){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    LinkedHashMap<String, Long> json = new LinkedHashMap<>();
    // For each month between from and till, get the amount of challenges
    for (LocalDate date = from;
         date.isBefore(till) || date.isEqual(till);
         date = date.plusMonths(1)) {
      json.put(
              date.getMonth().name() + "-" + date.getYear(),
              challengeRepository.countByCreatedAtBetweenAndDepartment_ParentCompanyId(date, date.plusMonths(1), companyId)
      );
    }
    return ResponseEntity.ok(json);
  }

  @GetMapping("/graph-data/company/{companyId}/challenges/status")
  public ResponseEntity<LinkedHashMap<String, Long>> getChallengesByStatusCountAndCompanyId(
          @PathVariable("companyId") Long companyId,
          @RequestAttribute("loggedInUser") User currentUser) {
    if (!currentUser.hasPermissionAtDepartment(
            Perm.COMPANY_GRAPH_READ, currentUser.department.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    if(!Objects.equals(currentUser.department.parentCompany.id, companyId)){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    LinkedHashMap<String, Long> json = new LinkedHashMap<>();

    for (ChallengeStatus challengeStatus : ChallengeStatus.values()) {
      json.put(challengeStatus.name(), challengeRepository.countByStatusAndDepartment_ParentCompanyId(challengeStatus, companyId));
    }
    return ResponseEntity.ok(json);
  }

  @GetMapping("/graph-data/company/{companyId}/challenge-inputs/filter/date")
  public ResponseEntity<LinkedHashMap<String, Long>> getChallengeInputsForRangeOfMonthsFilterAndCompanyId(
          @PathVariable("companyId") Long companyId,
          @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate from,
          @RequestParam(value = "till") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate till,
          @RequestAttribute("loggedInUser") User currentUser) {
    if (!currentUser.hasPermissionAtDepartment(
            Perm.COMPANY_GRAPH_READ, currentUser.department.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    if(!Objects.equals(currentUser.department.parentCompany.id, companyId)){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    LinkedHashMap<String, Long> json = new LinkedHashMap<>();
    // For each month between from and till, get the amount of challenges
    for (LocalDate date = from;
         date.isBefore(till) || date.isEqual(till);
         date = date.plusMonths(1)) {
      json.put(
              date.getYear() + " " + date.getMonth().name(),
              challengeInputRepository.countByCreatedAtBetweenAndChallenge_Department_ParentCompanyId(date, date.plusMonths(1).minusDays(1), companyId));
    }
    return ResponseEntity.ok(json);
  }

  @GetMapping("/graph-data/company/{companyId}/departments/users")
  public ResponseEntity<LinkedHashMap<String, Long>> getUsersByDepartmentsAndCompanyId(
          @PathVariable("companyId") Long companyId,
          @RequestAttribute("loggedInUser") User currentUser) {
    if (!currentUser.hasPermissionAtDepartment(
            Perm.COMPANY_GRAPH_READ, currentUser.department.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    if(!Objects.equals(currentUser.department.parentCompany.id, companyId)){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    LinkedHashMap<String, Long> json = new LinkedHashMap<>();
    Iterable<Department> departments = departmentRepository.findAllByParentCompanyId(companyId);

    for (Department department :  departments) {
      json.put(department.name, userRepository.countUsersByDepartmentId(department.id));
    }

    return ResponseEntity.ok(json);
  }
}
