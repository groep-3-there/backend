package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@EnableAutoConfiguration
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String name;
    public String info;
    public String tags;
    public Date createdAt;
    public Date lastSeen;
    public Long avatarImageId;
    public boolean isEmailPublic;
    public boolean isPhoneNumberPublic;
    public Date acceptedTosDate;

    public String email;
    public String phoneNumber;

    @ManyToOne
    public Role role;


    @ManyToMany
    public List<Challenge> favorites = new java.util.ArrayList<>();

    public User(String name) {
        this.name = name;
    }

    public boolean hasPermissionAtCompany(String m, Long companyId){
        if(!role.getCompany().id.equals(companyId)){
            //User is not part of the company, so he dos not have the permission
            return false;
        }

        for(Permission p : role.getPermissions()){
            if (p.codeName.equals(m)){
                return true;
            }
        }
        return false;
    }
    public boolean isInCompany(){
        return this.role != null;
    }


    public User() {

    }

}
