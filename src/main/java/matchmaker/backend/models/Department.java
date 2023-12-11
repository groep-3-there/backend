package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@EnableAutoConfiguration
@AllArgsConstructor
public class Department {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "department_id")
  @TableGenerator(name = "department_id", initialValue = 1000)
  public Long id;

  public String name;

  @ManyToOne public Company parentCompany;
  public LocalDate createdAt;
  public Department() {}

  public Department(String name, Company parentCompany) {
    this.name = name;
    this.parentCompany = parentCompany;
  }
}
