package matchmaker.backend.controllers;

import matchmaker.backend.constants.Perm;
import matchmaker.backend.models.Company;
import matchmaker.backend.models.Image;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.BranchRepository;
import matchmaker.backend.repositories.CompanyRepository;
import matchmaker.backend.repositories.ImageRepository;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class CompanyController {

  @Autowired private CompanyRepository repository;
  @Autowired private UserRepository userRepository;
  @Autowired private ImageRepository imageRepository;
  @Autowired private BranchRepository branchRepository;

  @GetMapping("/company")
  public ResponseEntity<Iterable<Company>> getCompanies() {
    Iterable<Company> company = repository.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(company);
  }

  @GetMapping("/company/{id}")
  public ResponseEntity<Optional<Company>> getCompanyById(@PathVariable("id") Long id) {
    Optional<Company> company = repository.findById(id);
    if (company.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    return ResponseEntity.status(HttpStatus.OK).body(company);
  }

  @GetMapping("/company/{id}/members")
  public ResponseEntity<Iterable<User>> getCompanyMembers(
      @PathVariable("id") Long id,
      @RequestAttribute(name = "loggedInUser", required = false) User currentUser) {
    Optional<Company> company = repository.findById(id);
    if (company.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    Iterable<User> members = userRepository.findAllByDepartment_ParentCompany_Id(id);
    List<User> serialized = new ArrayList<>();
    for (User member : members) {
      serialized.add(member.viewAs(currentUser));
    }

    return ResponseEntity.status(HttpStatus.OK).body(serialized);
  }

  @PutMapping("/company/{id}")
  public ResponseEntity<Company> UpdateCompanyProfile(
      @PathVariable("id") Long id,
      @RequestBody Company company,
      @RequestAttribute(name = "loggedInUser", required = false) User currentUser) {
    // check if the user can edit the profile
    if (currentUser == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    if (!currentUser.hasPermissionAtDepartment(Perm.COMPANY_MANAGE, currentUser.department.id)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    Optional<Company> targetCompany = repository.findById(id);

    if (targetCompany.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    Company checkedCompany = targetCompany.get();

    // check if the name is not blank or null
    if (company.getName() == null || company.getName().isBlank())
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    checkedCompany.setName(company.name);

    checkedCompany.setInfo(company.info);

    if (company.tags.endsWith(",")) {
      String tags = company.tags;
      company.tags = tags.substring(0, tags.length() - 1);
    }
    checkedCompany.setTags(company.tags);

    // get the current profile image if there is one
    Optional<Image> image = Optional.empty();
    if (company.profileImageId != null) {
      image = imageRepository.findById(company.profileImageId);
    }

    // check if the image the company wants is in the database
    if (image.isEmpty() && company.profileImageId != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    if (company.getBranch() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    // Only copy the branch if it exists
    if (branchRepository.existsById(company.getBranch().getId())) {
      checkedCompany.branch = branchRepository.findById(company.getBranch().getId()).get();
    }
    // set the profile image
    checkedCompany.setProfileImageId(company.profileImageId);

    // save the user to the database
    Company saveCompany = repository.save(checkedCompany);

    return ResponseEntity.status(HttpStatus.OK).body(saveCompany);
  }

  @GetMapping("/company/names")
  public ResponseEntity<Iterable<String>> getAllCompanyNames() {
    Iterable<Company> company = repository.findAll();
    List<Company> result =
        StreamSupport.stream(company.spliterator(), false).collect(Collectors.toList());
    List<String> names = new ArrayList<>();
    for (int i = 0; i < result.size(); i++) {
      if (!result.get(i).getName().isEmpty()) {
        names.add(result.get(i).getName());
      }
    }
    return ResponseEntity.status(HttpStatus.OK).body(names);
  }
}
