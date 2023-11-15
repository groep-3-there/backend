package matchmaker.backend.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;


@Entity
@Table(name = "permissions")
@EnableAutoConfiguration
@AllArgsConstructor
@Getter
@Setter
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "permission_id")
    @TableGenerator(name="permission_id", initialValue = 1000)
    public Long id;
    public String codeName;
    public String description;
    public String fancyName;

    public Permission() {

    }



    public Permission(String codeName, String description, String fancyName){
        this.codeName = codeName;
        this.description = description;
        this.fancyName = fancyName;
    }
}
