package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @UuidGenerator
    public UUID UUID;

    public String name;
    public String info;
    public String tags;
    public Date created_at;
    public Date last_seen;
    public Long avatar_image_id;
    public boolean is_email_public;
    public boolean is_phone_number_public;
    public Date accepted_tos_date;
    public Long role_id;
    public Long department_id;
    @ManyToOne
    Company company;

    public User(String name) {
        this.name = name;
    }

    public User() {

    }


    public UUID getUUID() {
        return UUID;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getTags() {
        return tags;
    }

    public void setCompanyId(Company company) {
        this.company = company;
    }
}
