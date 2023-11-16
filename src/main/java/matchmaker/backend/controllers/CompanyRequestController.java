package matchmaker.backend.controllers;

import matchmaker.backend.constants.Perm;
import matchmaker.backend.models.*;
import matchmaker.backend.repositories.CompanyRepository;
import matchmaker.backend.repositories.CompanyRequestRepository;
import matchmaker.backend.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@RestController
public class CompanyRequestController {

    @Autowired
    public CompanyRequestRepository repository;

    @Autowired
    public CompanyRepository companyRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping("/company/request")
    public Iterable<CompanyRequest> getRequests() {
        return repository.findAll();
    }

    @PostMapping("/company/request")
    public ResponseEntity<CompanyRequest> createCompanyRequest(
            @RequestBody CompanyRequest newCompanyRequest,
            @RequestAttribute(name = "loggedInUser", required = false) User currentUser){

        if(currentUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (currentUser.isInCompany()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        CompanyRequest checkedCompanyRequest = new CompanyRequest();

        if (newCompanyRequest.name == null || newCompanyRequest.name.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        checkedCompanyRequest.name = newCompanyRequest.name;

        if (newCompanyRequest.branch == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        checkedCompanyRequest.branch = newCompanyRequest.branch;

        //optional fields that can be null
        checkedCompanyRequest.tags = newCompanyRequest.tags;

        //set the date to now
        checkedCompanyRequest.requestedAt = new Date();

        //set company request owner
        checkedCompanyRequest.owner = currentUser;

        try {
            CompanyRequest savedCompanyRequest = repository.save(checkedCompanyRequest);
            return ResponseEntity.status(HttpStatus.OK).body(savedCompanyRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @PostMapping(path ="/company/request/{id}/accept")
    public ResponseEntity<CompanyRequest> gradeRequestAccept(
            @PathVariable("id") Long id,
            @RequestAttribute(name = "loggedInUser", required = false) User currentUser) {

        //check if the user has permission to accept a request
        if(currentUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if(currentUser.hasPermission(Perm.COMPANY_GRADE)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<CompanyRequest> request = repository.findById(id);
        if (request.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        CompanyRequest companyRequest = request.get();

        //create new company and delete request
        Company company = new Company();
        company.setName(companyRequest.name);
        company.setBranch(companyRequest.branch);
        company.setCreatedAt(new Date());
        company.setOwnerId(companyRequest.owner.id);
        company.setTags(companyRequest.tags);

        companyRepository.save(company);
        repository.delete(companyRequest);

        //create default department
        Department department = new Department("Management", company);
        department.createdAt = new Date();

        departmentRepository.save(department);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(path = "/company/request/{id}/reject")
    public ResponseEntity<CompanyRequest> gradeRequestReject(
            @PathVariable("id") Long id,
            @RequestAttribute(name = "loggedInUser", required = false) User currentUser){

        //check if user has permission to reject a request
        if(currentUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if(!currentUser.role.isMatchmaker) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<CompanyRequest> request = repository.findById(id);
        if (request.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        CompanyRequest companyRequest = request.get();

        //delete request
        repository.delete(companyRequest);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
