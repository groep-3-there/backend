package matchmaker.backend.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@Table(name = "permissions")
@EnableAutoConfiguration
@AllArgsConstructor
@Getter
@Setter
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String codeName;
    public String description;
    public String fancyName;

    public Permission() {

    }
}
