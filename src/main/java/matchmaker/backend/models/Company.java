package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "companies")
@EnableAutoConfiguration
@Getter
@Setter
public class Company {

    public Company(){

    }
    public Company(String name, User owner){
        this.owner = owner;
        this.name = name;
    }

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

    @OneToOne
    public User owner;
}
