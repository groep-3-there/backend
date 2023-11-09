package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import matchmaker.backend.constants.Perm;
import matchmaker.backend.controllers.ImageController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "roles")
@EnableAutoConfiguration
@AllArgsConstructor
@Getter
@Setter
public class Role {

    private static final Logger log = LoggerFactory.getLogger(Role.class);


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;


    public String name;
    public Date createdAt;
    public boolean isMatchmaker;

    @ManyToMany(fetch = FetchType.EAGER)
    public List<Permission> permissions = new java.util.ArrayList<>();

    public Role(String name, Company company, Department department) {
        this.name = name;
    }

    public void addPermission(Permission p){
        if(p.id == null){
            log.warn("!!!!! Permission must be fetched from database, skipping !!!!!");
            return;
        }

        this.permissions.add(p);
    }


    public Role(){

    }
}
