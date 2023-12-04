package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "roles")
@EnableAutoConfiguration
@AllArgsConstructor
@Getter
@Setter
public class Role {

  private static final Logger log = LoggerFactory.getLogger(Role.class);

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "role_id")
  @TableGenerator(name = "role_id", initialValue = 1000)
  public Long id;

  public String name;
  public LocalDate createdAt;
  public boolean isMatchmaker;
  public boolean isDepartmentAdmin;
  public boolean isAssignable;

  @ManyToMany(fetch = FetchType.EAGER)
  public List<Permission> permissions = new java.util.ArrayList<>();

  public Role(String name) {
    this.name = name;
  }

  public void addPermission(Permission p) {
    if (p.id == null) {
      log.warn("!!!!! Permission must be fetched from database, skipping !!!!!");
      return;
    }

    this.permissions.add(p);
  }

  public Role() {}
}
