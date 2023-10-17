package matchmaker.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
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
    public Long company_id;

    public User(String name){
        this.name = name;
    }
    public User(){

    }
}
