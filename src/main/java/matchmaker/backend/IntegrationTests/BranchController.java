package matchmaker.backend.IntegrationTests;

import matchmaker.backend.UnitTests.Branch;
import matchmaker.backend.repositories.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BranchController {

    @Autowired
    private BranchRepository repository;

    @GetMapping("/branch/all")
    public Iterable<Branch> getBranches() {
        return repository.findAll();
    }


}
