package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Date;

@Entity
@Table(name = "roles")
@EnableAutoConfiguration
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    @OneToOne
    public Company company;
    @OneToOne
    public Department department;
    public String name;
    public Date createdAt;
    public boolean isMatchmaker;

}
