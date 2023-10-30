package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Date;

@Entity
@Table(name = "departments")
@Getter
@Setter
@EnableAutoConfiguration
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String name;
    @OneToOne
    public Company parentCompany;
    public Date createdAt;
}
