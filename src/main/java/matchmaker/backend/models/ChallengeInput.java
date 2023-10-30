package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.lang.reflect.Type;
import java.util.Date;

@Entity
@Setter
@Getter
@EnableAutoConfiguration
@Table(name = "challengeinputs")
public class ChallengeInput {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @OneToOne
    public User author;

    @OneToOne
    public Challenge challenge;

    public Enum type;

    public String text;

    public boolean isChosenAnswer;

    public Date createdAt;
}
