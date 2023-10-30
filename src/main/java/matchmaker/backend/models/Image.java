package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@Table(name = "images")
@Getter
@Setter
@EnableAutoConfiguration
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @ManyToOne
    public Challenge challenge;

    @ManyToOne
    public ChallengeInput challengeInput;

    public String data;
}

