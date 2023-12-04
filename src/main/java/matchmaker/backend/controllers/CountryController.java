package matchmaker.backend.controllers;

import matchmaker.backend.models.Branch;
import matchmaker.backend.models.Country;
import matchmaker.backend.repositories.BranchRepository;
import matchmaker.backend.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryController {

  @Autowired private CountryRepository repository;

  @GetMapping("/country/all")
  public Iterable<Country> getCountries() {
    return repository.findAll();
  }
}
