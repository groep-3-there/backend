package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import matchmaker.backend.constants.ChallengeReactionType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@EnableAutoConfiguration
@AllArgsConstructor
@Table(name = "challengeinputs")
public class ChallengeInput {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "challengeinput_id")
  @TableGenerator(name = "challengeinput_id", initialValue = 1000)
  public Long id;

  @ManyToOne public User author;

  @ManyToOne public Challenge challenge;

  @Enumerated(EnumType.ORDINAL)
  public ChallengeReactionType type;

  @Column(length = 65535, columnDefinition = "Text")
  public String text;

  public boolean isChosenAnswer;

  public LocalDate createdAt;

  public ChallengeInput() {}

  public ChallengeInput(String name) {
    this.text = name;
  }
}
