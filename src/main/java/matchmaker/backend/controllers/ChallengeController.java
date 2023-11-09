package matchmaker.backend.controllers;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.constants.Perm;
import matchmaker.backend.models.*;
import matchmaker.backend.repositories.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Challenge> getChallengeById(
            @PathVariable("id") Long id,
            @RequestAttribute("loggedInUser") User currentUser) {
        Optional<Challenge> target = repository.findById(id);
        if (target.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Challenge challenge = target.get();
        if (!challenge.canBeSeenBy(currentUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(challenge);
    }

    //Discuss, {id} or update?
    @PutMapping("/challenge/update")
    public HttpStatus updateChallenge(
            @RequestBody Challenge challengeToUpdate,
            @RequestAttribute("loggedInUser") User currentUser) {
        Optional<Challenge> target = repository.findById(challengeToUpdate.id);
        if (target.isEmpty()) {

            return HttpStatus.EXPECTATION_FAILED;
        }

        //Grab the same challenge from the database to be sure we have valid data.
        Challenge challengeInDatabase = target.get();
        if (!challengeInDatabase.canBeEditedBy(currentUser)) {
            return HttpStatus.UNAUTHORIZED;
        }

        repository.save(challengeToUpdate);
        return HttpStatus.OK;
    }

    /**
     * Create a new challenge
     * @param newChallenge - Challenge object with the data to create
     * @param currentUser - the user that is logged in
     * @return the created challenge so the user can be redirected its id
     * or UNAUTHORIZED if the user is not authorized
     * or BAD_REQUEST if the input is invalid
     */
    @PostMapping(path = "/challenge")
    public ResponseEntity<Challenge> createChallenge(
            @RequestBody Challenge newChallenge,
            @RequestAttribute("loggedInUser") User currentUser) {
        if (!currentUser.isInCompany()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!currentUser.hasPermissionAtCompany(Perm.CHALLENGE_MANAGE, currentUser.role.company.id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Challenge checkedChallenge = new Challenge();

        //Only copy values we trust from the end user. If user passes id, it is ignored.
        if (newChallenge.id != null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        if (newChallenge.title == null || newChallenge.title.isBlank())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        checkedChallenge.title = newChallenge.title;

        if (newChallenge.summary == null || newChallenge.summary.isBlank())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        checkedChallenge.summary = newChallenge.summary;

        if (newChallenge.description == null || newChallenge.description.isBlank())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        checkedChallenge.description = newChallenge.description;

        if (newChallenge.contactInformation == null || newChallenge.contactInformation.isBlank())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        checkedChallenge.contactInformation = newChallenge.contactInformation;

        //if user passed a concluding remark, it is not sent from the Vue GUI.
        if (newChallenge.concludingRemarks != null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        checkedChallenge.concludingRemarks = newChallenge.concludingRemarks;

        if (newChallenge.status == null) {
            newChallenge.status = ChallengeStatus.OPEN_VOOR_IDEEEN;
        } else {
            checkedChallenge.status = newChallenge.status;
        }

        if (newChallenge.visibility == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        checkedChallenge.visibility = newChallenge.visibility;

        //optional fields that can be null
        checkedChallenge.bannerImageId = newChallenge.bannerImageId;
        checkedChallenge.imageAttachmentsIds = newChallenge.imageAttachmentsIds;
        checkedChallenge.endDate = newChallenge.endDate;

        checkedChallenge.tags = newChallenge.tags;
        //Remove the last comma, if there is one
        if (checkedChallenge.tags.endsWith(",")) {
            String tags = checkedChallenge.tags;
            checkedChallenge.tags = tags.substring(0, tags.length() - 1);
        }

        //set the date to now
        checkedChallenge.createdAt = new Date();

        //Set this based on the session, so no bad input can set the author, company & department
        checkedChallenge.author = currentUser;
        checkedChallenge.company = currentUser.role.company;
        checkedChallenge.department = currentUser.role.department;
        checkedChallenge.createdAt = new Date();

        try {
            Challenge savedChallenge = repository.save(checkedChallenge);
            return ResponseEntity.status(HttpStatus.OK).body(savedChallenge);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * / Get 10 challenges per page by search criteria (filters)
     * / The method will return a Page object with the challenges for that page
     *
     * @param query   - words to search for in the title, tags, description and summary
     * @param company - the company name to search for
     * @param branche - the branche name to search for
     * @param sort    - the sort order, can be Newest_first or deadline_closest_first
     *                / if empty, Newest_first will be used
     * @param page    - the page number to return
     *                / if empty, the first page (0) will be returned
     */
    @GetMapping("/challenge/search")
    public Page<Challenge> search(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "company", required = false) List<String> company,
            @RequestParam(value = "branche", required = false) List<String> branche,
            @RequestParam(value = "sort", defaultValue = "Newest_first") String sort,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        Specification<Challenge> specification = getSpecification(query, company, branche);

        //sort the results in this order: the most recent ones and the ones where the deadline is closest
        Sort sortOrder = Sort.by(Sort.Direction.ASC, "createdAt");

        if ("Newest_first".equalsIgnoreCase(sort)) {
            sortOrder = Sort.by(Sort.Direction.DESC, "createdAt");
        } else if ("deadline_closest_first".equalsIgnoreCase(sort)) {
            sortOrder = Sort.by(Sort.Direction.ASC, "endDate");
        }

        int pageSize = 10;

        // Create a Pageable object to control pagination
        Pageable pageable = PageRequest.of(page, pageSize, sortOrder); // Page size is set to 10

        // Use a repository method to find challenges with the criteria, sorted and paginated
        return repository.findAll(specification, pageable);
    }

    /**
     * gets the amount of results for the search criteria (filters)
     *
     * @param query   - words to search for in the title, tags, description and summary
     * @param company - the company name to search for
     */
    @GetMapping("/challenge/search/count")
    public long searchCount(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "company", required = false) List<String> company,
            @RequestParam(value = "branche", required = false) List<String> branche) {
        Specification<Challenge> specification = getSpecification(query, company, branche);
        return repository.findAll(specification).size();
    }

    /**
     * / Private method to create a Specification object based on the search criteria (filters)
     */
    private Specification<Challenge> getSpecification(
            String query,
            List<String> company,
            List<String> branche) {
        return (root, query1, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            //find on the query, this includes: title, tags, description and summary
            if (query != null && !query.isEmpty()) {
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
    }
}