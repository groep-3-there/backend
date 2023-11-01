package matchmaker.backend.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.sql.Blob;

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
    @Nullable
    public Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @Nullable
    public ChallengeInput challengeInput;

    @Nullable
    public String data;

    public Image() {

    }
    public Image(String photoData, String data){
        this.data = data;
    }
}

