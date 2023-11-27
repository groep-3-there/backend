package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@Table(name = "tags")
@EnableAutoConfiguration
@AllArgsConstructor
@Getter
@Setter
public class Tag {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "tag_id")
  @TableGenerator(name = "tag_id", initialValue = 1000)
  public Long id;

  public String name;

  public Tag(String name) {
    this.name = name;
  }

  public Tag() {}
}
