package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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

  @Column(length = 65535, columnDefinition = "Text")
  public String info;

  @Nullable public Long profileImageId;
  @Nullable public Long bannerImageId;
  public String tags;

  @ManyToOne public Branch branch;
  public LocalDate createdAt;

  @ElementCollection(fetch = FetchType.EAGER)
  public List<Long> followerIds;

  public Long ownerId;

  @ManyToOne public Country country;
  public Company() {}

  public Company(String name) {
    this.name = name;
  }
}
