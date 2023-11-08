package matchmaker.backend.UnitTests;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Date;

@Entity
@Table(name = "departments")
@Getter
@Setter
@EnableAutoConfiguration
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String name;

    @ManyToOne
    public Company parentCompany;
    public Date createdAt;

    public Department() {

    }
    public Department(String name, Company parentCompany){
        this.name = name;
        this.parentCompany = parentCompany;
    }
}
