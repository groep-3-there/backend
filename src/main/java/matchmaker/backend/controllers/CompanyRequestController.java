package matchmaker.backend.controllers;

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

import java.util.Date;
import java.util.Optional;

@RestController
public class CompanyRequestController {

  @Autowired public CompanyRequestRepository repository;

  @Autowired public CompanyRepository companyRepository;

  @Autowired private DepartmentRepository departmentRepository;

  @Autowired private BranchRepository branchRepository;

  @Autowired private RoleRepository roleRepository;
  @Autowired private UserRepository userRepository;

  @GetMapping("/company/request")
  public Page<CompanyRequest> getRequests(
      @RequestAttribute(name = "loggedInUser", required = false) User currentUser,
      @RequestParam(defaultValue = "0") int page) {
    if (currentUser == null) {
      return null;
    }
    if (!currentUser.hasPermission(Perm.COMPANY_GRADE)) {
      return null;
    }

    Sort sortOder = Sort.by("requestedAt").ascending();

    int pageSize = 3;
    Pageable pageable = PageRequest.of(page, pageSize, sortOder);
    return repository.findAll(pageable);
  }

  @PostMapping("/company/request")
  public ResponseEntity createCompanyRequest(
      @RequestBody CompanyRequest newCompanyRequest,
      @RequestAttribute(name = "loggedInUser", required = false) User currentUser) {

    if (currentUser == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
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
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    checkedCompanyRequest.branch = newCompanyRequest.branch;

    // optional fields that can be null
    checkedCompanyRequest.tags = newCompanyRequest.tags;
    if (checkedCompanyRequest.tags.endsWith(",")) {
      checkedCompanyRequest.tags =
          checkedCompanyRequest.tags.substring(0, checkedCompanyRequest.tags.length() - 1);
    }

    // set the date to now
    checkedCompanyRequest.requestedAt = new Date();

    // set company request owner
    checkedCompanyRequest.owner = currentUser;

    try {
      CompanyRequest savedCompanyRequest = repository.save(checkedCompanyRequest);
      return ResponseEntity.status(HttpStatus.OK).body(savedCompanyRequest);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
    }
  }

  @PostMapping(path = "/company/request/{id}/accept")
  public ResponseEntity gradeRequestAccept(
      @PathVariable("id") Long id,
      @RequestAttribute(name = "loggedInUser", required = false) User currentUser) {

    // check if the user has permission to accept a request
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

    // create new company and delete request
    Company company = new Company();
    company.setName(companyRequest.name);
    company.setBranch(companyRequest.branch);
    company.setCreatedAt(new Date());
    company.setOwnerId(companyRequest.owner.id);
    company.setTags(companyRequest.tags);

    companyRepository.save(company);
    repository.delete(companyRequest);

    // create default department
    Department department = new Department("Management", company);
    department.createdAt = new Date();
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

    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  @PostMapping(path = "/company/request/{id}/reject")
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

    // delete request
    repository.delete(companyRequest);
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }
}
