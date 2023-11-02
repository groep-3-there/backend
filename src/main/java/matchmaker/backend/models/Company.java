package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Date;

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
    public Long profileImageId;
    public Long bannerImagId;
    public String tags;
    public String branch;
    public Date createdAt;

    @OneToOne(fetch = FetchType.EAGER)
    public User owner;

    public Company() {

    }
    public Company(String name){
        this.name = name;
    }
    public Company(String name, User owner){
        this.name = name;
        this.owner = owner;
    }
}
