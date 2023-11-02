package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import matchmaker.backend.constants.ChallengeStatus;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Date;

@Entity
@Table(name = "challenges")
@EnableAutoConfiguration
@AllArgsConstructor
@Getter
@Setter
public class Challenge {
    public Challenge(){

    }

    public Challenge(String title, User author){
        this.author = author;
        this.title = title;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @ManyToOne
    public User author;

    @ManyToOne
    public Department department;

    @ManyToOne
    public Company company;

    public String contactInformation;
    public String title;
    public String description;
    public Long bannerImageId;
    public String concludingRemarks;
    public String summary;

    @Enumerated(EnumType.ORDINAL)
    public ChallengeStatus status;
    public Date createdAt;
    public Date endDate;
    public String tags;

    @ManyToOne
    public Branch branch;
    public boolean canReact;
    public boolean isPublicVisible;
    public boolean isPublicReactable;
}
