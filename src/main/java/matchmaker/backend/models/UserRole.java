package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@Table(name = "userroles")
@EnableAutoConfiguration
@Getter
@Setter
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @OneToOne
    public User user;
    @OneToOne
    public Role role;
    @OneToOne
    public Company company;

}
