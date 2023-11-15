package matchmaker.backend.controllers;

import matchmaker.backend.models.Company;
import matchmaker.backend.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
