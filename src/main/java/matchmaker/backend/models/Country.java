package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.time.LocalDate;

@Entity
@Table(name = "countries")
@Getter
@Setter
@EnableAutoConfiguration
@AllArgsConstructor
public class Country {

    @Id
    public String code;

    public String name;

    public Country(){};
}
