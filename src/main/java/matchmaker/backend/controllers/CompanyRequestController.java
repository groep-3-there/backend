package matchmaker.backend.controllers;

import matchmaker.backend.constants.Perm;
import matchmaker.backend.models.Challenge;
import matchmaker.backend.models.Company;
import matchmaker.backend.models.CompanyRequest;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.CompanyRepository;
import matchmaker.backend.repositories.CompanyRequestRepository;
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

    @GetMapping("/company/request")
    public Iterable<CompanyRequest> getRequests() {
        return repository.findAll();
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
