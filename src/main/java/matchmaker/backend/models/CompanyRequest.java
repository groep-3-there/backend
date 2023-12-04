package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.time.LocalDate;

@Entity
@Table(name = "companyrequests")
@Getter
@Setter
@EnableAutoConfiguration
@AllArgsConstructor
public class CompanyRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "companyrequest_id")
  @TableGenerator(name = "companyrequest_id", initialValue = 1000)
  public Long id;

  public String name;

  @ManyToOne public Branch branch;

  public String tags;
  public LocalDate requestedAt;

  @OneToOne public User owner;

  @ManyToOne public Country country;

  public CompanyRequest() {}
}
