package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Date;

@Entity
@Table(name = "companies")
@EnableAutoConfiguration
@AllArgsConstructor
@Getter
@Setter
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "company_id")
  @TableGenerator(name = "company_id", initialValue = 1000)
  public Long id;

  public String name;
  public String info;
  public Long profileImageId;
  public Long bannerImageId;
  public String tags;

  @ManyToOne public Branch branch;
  public Date createdAt;

  public Long ownerId;

  public Company() {}

  public Company(String name) {
    this.name = name;
  }
}
