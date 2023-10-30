package matchmaker.backend.controllers;

import matchmaker.backend.models.Company;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.CompanyRepository;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CompanyController {

    @Autowired
    private CompanyRepository repository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/companies")
    public Iterable<Company> getCompanies() {
        return repository.findAll();
    }

    @GetMapping("/companies/join/{userId}/{companyId}")
    public void joinCompany(@PathVariable UUID userId, @PathVariable Long companyId){
    }
}
