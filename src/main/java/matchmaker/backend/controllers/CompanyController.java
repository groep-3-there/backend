package matchmaker.backend.controllers;

import matchmaker.backend.models.Challenge;
import matchmaker.backend.models.Company;
import matchmaker.backend.models.Department;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.CompanyRepository;
import matchmaker.backend.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class CompanyController {

    @Autowired
    private CompanyRepository repository;

    @GetMapping("/company")
    public ResponseEntity<Iterable<Company>> getCompanies() {
        Iterable<Company> company = repository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(company);
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<Optional<Company>> getCompanyById(@PathVariable("id") Long id){
        Optional<Company> company = repository.findById(id);
        if(company.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(company);
    }

    @GetMapping("/company/names")
    public ResponseEntity<Iterable<String>> getAllCompanyNames(){
        Iterable<Company> company = repository.findAll();
        List<Company> result =
                StreamSupport.stream(company.spliterator(), false)
                        .collect(Collectors.toList());
        List<String> names = new ArrayList<>();
        for (int i = 0; i < result.size(); i++){
            if(!result.get(i).getName().isEmpty()) {
                names.add(result.get(i).getName());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(names);
    }
}
