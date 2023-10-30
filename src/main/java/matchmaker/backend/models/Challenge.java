package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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

    @OneToOne
    public User author;

    @OneToOne
    public Department department;

    @OneToOne
    public Company company;
    public String contactInformation;
    public String title;
    public String description;
    public String banner;
    public String concludingRemarks;
    public String summary;
    public Enum status;
    public Date createdAt;
    public Date endDate;
    public String tags;
    public Enum branch;
    public boolean canReact;
    public boolean isPublicVisible;
    public boolean isPublicReactable;
}
