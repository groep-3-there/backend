package matchmaker.backend.repositories;

import matchmaker.backend.models.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CountryRepository extends CrudRepository<Country, Long> {
  Optional<Country> findByCode(String code);

  Iterable<Country> findAll();
}
