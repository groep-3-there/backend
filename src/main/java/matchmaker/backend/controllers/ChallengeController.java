package matchmaker.backend.controllers;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import matchmaker.backend.constants.Perm;
import matchmaker.backend.models.*;
import matchmaker.backend.repositories.ChallengeRepository;
import matchmaker.backend.repositories.ImageRepository;
import matchmaker.backend.repositories.UserRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ChallengeController {

    @Autowired
    private ChallengeRepository repository;

    @GetMapping("/challenge")
    public Iterable<Challenge> getChallenges() {
        return repository.findAll();
    }

    @GetMapping("/challenge/{id}")
    public ResponseEntity<Optional<Challenge>> getChallengeById(@PathVariable("id")Long id, @RequestAttribute("loggedInUser") User currentUser) {
        if(!currentUser.hasPermissionAtCompany(currentUser.company.getId(), Perm.CHALLENGE_READ)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Optional.empty());
        }
        return ResponseEntity.status(HttpStatus.OK).body(repository.findById(id));
    }

    @PutMapping("/challenge/update")
    public HttpStatus updateChallenge(@RequestBody Challenge challengeToUpdate, @RequestAttribute("loggedInUser") User currentUser){
        if(!currentUser.hasPermissionAtCompany(currentUser.company.getId(), Perm.CHALLENGE_MANAGE)){
            return HttpStatus.FORBIDDEN;
        }
        Optional<Challenge> challenge = repository.findById(challengeToUpdate.id);
        if (challenge.isEmpty()){
            return HttpStatus.EXPECTATION_FAILED;
        }
        repository.save(challengeToUpdate);
        return HttpStatus.OK;
    }

    @PostMapping(path = "/challenge")
    public ResponseEntity<Optional<Challenge>> createChallenge(@RequestBody Challenge newChallenge, @RequestAttribute("loggedInUser") User currentUser) {
        Challenge checkedChallenge = new Challenge();
        if(currentUser.hasPermissionAtCompany(currentUser.company.getId(), Perm.CHALLENGE_MANAGE)) {

            checkedChallenge.contactInformation = newChallenge.contactInformation;
            checkedChallenge.title = newChallenge.title;
            checkedChallenge.description = newChallenge.description;
            checkedChallenge.bannerImageId = newChallenge.bannerImageId;
            checkedChallenge.concludingRemarks = newChallenge.concludingRemarks;
            checkedChallenge.summary = newChallenge.summary;
            checkedChallenge.status = newChallenge.status;
            checkedChallenge.endDate = newChallenge.endDate;
            checkedChallenge.tags = newChallenge.tags;
            checkedChallenge.branch = newChallenge.branch;
            checkedChallenge.visibility = newChallenge.visibility;

            Challenge savedChallenge = repository.save(checkedChallenge);
            return ResponseEntity.status(HttpStatus.CREATED).body(Optional.of(savedChallenge));
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Optional.empty());
        }
    }

    @GetMapping("/challenge/search")
    public Iterable<Challenge> search(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "company", required = false) List<String> company,
            @RequestParam(value = "branche", required = false) List<String> branche,
            @RequestParam(value = "sort", defaultValue = "Newest_first") String sort) {

        Specification<Challenge> spec = (root, query1, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            //find on the query, this includes: title, tags, description and summary
            if (query != null && !query.isEmpty()){
                Predicate titlePredicate = builder.like(root.get("title"), "%" + query + "%");
                Predicate tagsPredicate = builder.like(root.get("tags"), "%" + query + "%");
                Predicate descriptionPredicate = builder.like(root.get("description"), "%" + query + "%");
                Predicate summaryPredicate = builder.like(root.get("summary"), "%" + query + "%");
                predicates.add(builder.or(titlePredicate, tagsPredicate, descriptionPredicate, summaryPredicate));
            }

            //find on the company name
            if (company != null && !company.isEmpty()) {
                Join<Challenge, Company> companyJoin = root.join("company");
                Expression<String> companyNameExpression = companyJoin.get("name");
                predicates.add(companyNameExpression.in(company));
            }

            //find on the branche name
            if (branche != null && !branche.isEmpty()) {
                Join<Challenge, Branch> brancheJoin = root.join("branch");
                Expression<String> brancheNameExpression = brancheJoin.get("name");
                predicates.add(brancheNameExpression.in(branche));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };

        //sort the results in this order: the most recent ones and the ones where the deadline is closest
        Sort sortOrder = Sort.by(Sort.Direction.ASC, "createdAt");

        if ("Newest_first".equalsIgnoreCase(sort)) {
            sortOrder = Sort.by(Sort.Direction.DESC, "createdAt");
        }
        else if ("deadline_closest_first".equalsIgnoreCase(sort)) {
            sortOrder = Sort.by(Sort.Direction.ASC, "endDate");
        }

        //find all the challenges with the criteria
        return repository.findAll(spec, sortOrder);
    }
}