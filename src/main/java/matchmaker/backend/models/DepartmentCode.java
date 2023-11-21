package matchmaker.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@Table(name = "department_code")
@Getter
@Setter
@EnableAutoConfiguration
@AllArgsConstructor
public class DepartmentCode {

    @Id
    public String code;

    @OneToOne
    public Department department;

    public DepartmentCode() {}
}
