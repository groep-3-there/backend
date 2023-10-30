package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Date;

@Entity
@Table(name = "companyrequests")
@Getter
@Setter
@EnableAutoConfiguration
@AllArgsConstructor
public class CompanyRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String name;

    @OneToOne
    public Branch branch;

    public String tags;
    public Date requestedAt;

    @OneToOne
    public User owner;

    public CompanyRequest() {

    }
}
