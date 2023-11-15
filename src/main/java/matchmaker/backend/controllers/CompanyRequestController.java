package matchmaker.backend.controllers;

import matchmaker.backend.models.Challenge;
import matchmaker.backend.models.Company;
import matchmaker.backend.models.CompanyRequest;
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

    @PostMapping("/company/request/{id}/grade")
    public ResponseEntity<CompanyRequest> gradeRequest(
            @PathVariable("id") Long id,
            @RequestBody String grade) {
        CompanyRequest request = repository.findById(id).get();
        if (request.id == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (grade.equals("Accept")) {
            //create new company and delete request
            Company company = new Company();
            company.setName(request.name);
            company.setBranch(request.branch);
            company.setCreatedAt(new Date());
            company.setOwnerId(request.owner.id);
            company.setTags(request.tags);

            companyRepository.save(company);
            repository.delete(request);

            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        else if (grade.equals("Reject")){
            //delete request
            repository.delete(request);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
