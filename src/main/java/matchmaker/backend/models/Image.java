package matchmaker.backend.models;

import jakarta.annotation.Nullable;
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

    @Lob
    public byte[] photoData;

    @ManyToOne
    @JoinColumn(name = "author_id")
    public User author;

    @ManyToOne
    @Nullable
    public Challenge attachmentForChallenge;

    public Image() {

    }

    public Image(byte[] photoData) {
        this.photoData = photoData;
    }
}

