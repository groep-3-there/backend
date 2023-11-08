package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import matchmaker.backend.constants.ChallengeReactionType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import java.util.Date;

@Entity
@Setter
@Getter
@EnableAutoConfiguration
@AllArgsConstructor
@Table(name = "challengeinputs")
public class ChallengeInput {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @ManyToOne
    public User author;

    @ManyToOne
    public Challenge challenge;

    @Enumerated(EnumType.ORDINAL)
    public ChallengeReactionType type;

    public String text;

    public boolean isChosenAnswer;

    public Date createdAt;

    public ChallengeInput() {

    }
}
