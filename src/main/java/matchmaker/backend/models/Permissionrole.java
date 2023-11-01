//package matchmaker.backend.models;
//
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//
//@Entity
//@Table(name = "permissionroles")
//@EnableAutoConfiguration
//@AllArgsConstructor
//@Getter
//@Setter
//public class Permissionrole {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    public Long id;
//
//    @ManyToOne
//    public Role role;
//
//    @ManyToOne
//    public Permission permission;
//
//    public Permissionrole() {
//
//    }
//}