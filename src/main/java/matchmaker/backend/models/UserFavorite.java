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
//@Table(name = "user_favorite")
//@EnableAutoConfiguration
//@AllArgsConstructor
//@Getter
//@Setter
//public class UserFavorite {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    public Long id;
//
//    @ManyToMany
//    public Challenge challenge;
//
//    @ManyToMany
//    public User user;
//
//    public UserFavorite() {
//
//    }
//}
