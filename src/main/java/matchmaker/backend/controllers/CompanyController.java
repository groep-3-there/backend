package matchmaker.backend.controllers;

import matchmaker.backend.models.Company;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.CompanyRepository;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
public class CompanyController {

    @Autowired
    private CompanyRepository repository;

    @GetMapping("/company")
    public Iterable<Company> getCompanies() {
        return repository.findAll();
    }

    @GetMapping("/company/{id}")
    public Optional<Company> getCompanyById(@PathVariable("id") Long id){
        return repository.findById(id);
    }


    @GetMapping("/company/join/{userId}/{companyId}")
    public void joinCompany(@PathVariable UUID userId, @PathVariable Long companyId){
    }
}