package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@EnableAutoConfiguration
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    public Long id;

    public String name;
    public String info;
    public String tags;
    public Date createdAt;
    public Date lastSeen;
    @OneToOne
    public Image avatarImage;
    public boolean isEmailPublic;
    public boolean isPhoneNumberPublic;
    public Date acceptedTosDate;
    @OneToOne
    public Role role;
    @OneToOne
    public Department department;

    @OneToOne
    public Company company;

    public User(String name) {
        this.name = name;
    }

    public User() {

    }

}
