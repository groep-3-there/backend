package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "companies")
@EnableAutoConfiguration
@AllArgsConstructor
@Getter
@Setter
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String name;
    public String info;
    public String profileImage;
    public String bannerImage;
    public String tags;
    public String branch;
    public Date createdAt;

    @OneToOne(fetch=FetchType.EAGER)
    public User owner;

    public Company() {

    }
    public Company(String name, User owner){
        this.name = name;
        this.owner = owner;
    }
}
