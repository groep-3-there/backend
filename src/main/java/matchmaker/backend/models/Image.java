package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@Table(name = "images")
@Getter
@Setter
@EnableAutoConfiguration
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    public Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    public ChallengeInput challengeInput;

    public String data;

    @Lob
    public byte[] photoData;

    public Image() {

    }

    public Image(String photoData, String data) {
        this.data = data;
    }
}

