package matchmaker.backend.controllers;

import matchmaker.backend.NotificationService;
import matchmaker.backend.constants.DefaultRoleId;
import matchmaker.backend.constants.Perm;
import matchmaker.backend.models.*;
import matchmaker.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
public class CompanyRequestController {

  @Autowired public CompanyRequestRepository repository;

  @Autowired public CompanyRepository companyRepository;

  @Autowired private DepartmentRepository departmentRepository;

  @Autowired private BranchRepository branchRepository;

  @Autowired private RoleRepository roleRepository;
  @Autowired private UserRepository userRepository;

  @Autowired private CountryRepository countryRepository;

  @Autowired private NotificationService notificationService;

  @GetMapping("/company-request")
  public ResponseEntity<Page<CompanyRequest>> getRequests(
      @RequestAttribute("loggedInUser") User currentUser,
      @RequestParam(value = "page", defaultValue = "0") int page)
  {
    if (!currentUser.hasPermission(Perm.COMPANY_GRADE)) {
      return null;
    }
    Sort sortOder = Sort.by("requestedAt").ascending();

    int pageSize = 3;
    Pageable pageable = PageRequest.of(page, pageSize, sortOder);

    return ResponseEntity.ok(repository.findAll(pageable));
  }

  @PostMapping("/company-request")
  public ResponseEntity<CompanyRequest> createCompanyRequest(
      @RequestBody CompanyRequest newCompanyRequest,
      @RequestAttribute("loggedInUser") User currentUser)
  {
    if (currentUser.isInCompany()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    CompanyRequest checkedCompanyRequest = new CompanyRequest();

    if (newCompanyRequest.name == null || newCompanyRequest.name.isBlank()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    checkedCompanyRequest.name = newCompanyRequest.name;

    if (newCompanyRequest.branch == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    if (!branchRepository.existsById(newCompanyRequest.getBranch().getId())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    if (branchRepository.existsById(newCompanyRequest.getBranch().getId())) {
      checkedCompanyRequest.branch =
          branchRepository.findById(newCompanyRequest.getBranch().getId()).get();
    }

    // optional fields that can be null
    checkedCompanyRequest.tags = newCompanyRequest.tags;
    if (checkedCompanyRequest.tags.endsWith(",")) {
      checkedCompanyRequest.tags =
          checkedCompanyRequest.tags.substring(0, checkedCompanyRequest.tags.length() - 1);
    }

    // set the date to now
    checkedCompanyRequest.requestedAt = LocalDate.now();

    // set company request owner
    checkedCompanyRequest.owner = currentUser;

    // Country code, we get the country only using the code
    String countryCodeInput = newCompanyRequest.country.getCode();
    Optional<Country> country = countryRepository.findByCode(countryCodeInput);
    if (country.isPresent()) {
      checkedCompanyRequest.country = country.get();
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    try {
      CompanyRequest savedCompanyRequest = repository.save(checkedCompanyRequest);
      return ResponseEntity.ok(savedCompanyRequest);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
    }
  }

  @PostMapping(path = "/company-request/{id}/accept")
  public ResponseEntity<String> gradeRequestAccept(
      @PathVariable("id") Long id,
      @RequestAttribute("loggedInUser") User currentUser)
  {
    if (!currentUser.hasPermission(Perm.COMPANY_GRADE)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    Optional<CompanyRequest> request = repository.findById(id);
    if (request.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    CompanyRequest companyRequest = request.get();

    // create new company and delete request
    Company company = new Company();
    company.setName(companyRequest.name);
    company.setBranch(companyRequest.branch);
    company.setCreatedAt(LocalDate.now());
    company.setOwnerId(companyRequest.owner.id);
    company.setTags(companyRequest.tags);
    company.setCountry(companyRequest.country);

    companyRepository.save(company);
    repository.delete(companyRequest);

    // create default department
    Department department = new Department("Management", company);
    department.createdAt = LocalDate.now();
    Optional<Role> companyOwner = roleRepository.findById(DefaultRoleId.COMPANY_BEHEERDER);

    // check if the role exists
    if (companyOwner.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
          .body(
              "De opgevraagde rol is niet gevonden in de database. Neem contact op met de"
                  + " systeembeheerder");
    }

    // give owner department manager role
    companyRequest.getOwner().setRole(companyOwner.get());

    departmentRepository.save(department);

    companyRequest.owner.setDepartment(department);
    userRepository.save(companyRequest.owner);

    notificationService.sendSuccesfulCompanyGradeNotificationForOwner(companyRequest.getOwner());

    return ResponseEntity.ok(null);
  }

  @PostMapping(path = "/company-request/{id}/reject")
  public ResponseEntity<CompanyRequest> gradeRequestReject(
      @PathVariable("id") Long id,
      @RequestAttribute(name = "loggedInUser", required = false) User currentUser) {

    // check if user has permission to reject a request
    if (currentUser == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    if (!currentUser.hasPermission(Perm.COMPANY_GRADE)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    Optional<CompanyRequest> request = repository.findById(id);
    if (request.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    CompanyRequest companyRequest = request.get();
    notificationService.sendRejectedCompanyGradeNotificationForOwner(companyRequest.getOwner());

    // delete request
    repository.delete(companyRequest);
    return ResponseEntity.ok(null);
  }
}
