package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roles")
@EnableAutoConfiguration
@AllArgsConstructor
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @ManyToOne
    public Company company;

    @ManyToOne
    public Department department;
    public String name;
    public Date createdAt;
    public boolean isMatchmaker;

    @ManyToMany
    public List<Permission> permissions = new java.util.ArrayList<>();

    public Role(String name, Company company, Department department) {
        this.name = name;
        this.company = company;
        this.department = department;
    }
    public Role(){

    }
}