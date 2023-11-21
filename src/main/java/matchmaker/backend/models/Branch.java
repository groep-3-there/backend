package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@Getter
@Setter
@AllArgsConstructor
@EnableAutoConfiguration
@Table(name = "branches")
public class Branch {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "branche_id")
  @TableGenerator(name = "branche_id", initialValue = 1000)
  public Long id;

  public String name;

  public Branch() {}
}
