package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@EnableAutoConfiguration
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "user_id")
  @TableGenerator(name = "user_id", initialValue = 1000)
  public Long id;

  public String firebaseId;
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

  @ManyToOne public Role role;

  @ManyToOne public Department department;

  public User(String name) {
    this.name = name;
  }

  public boolean hasPermissionAtDepartment(String m, Long departmentId) {
    if (this.role.isMatchmaker) {
      return true;
    }
    if (!department.id.equals(departmentId)) {
      // User is not part of the company, so he does not have the permission
      return false;
    }

    for (Permission p : role.getPermissions()) {
      if (p.codeName.equals(m)) {
        return true;
      }
    }
    return false;
  }

  public boolean isInCompany() {
    return this.department != null;
  }

  public User() {}

  public User(User u) {
    this.id = u.id;
    this.firebaseId = u.firebaseId;
    this.name = u.name;
    this.info = u.info;
    this.tags = u.tags;
    this.createdAt = u.createdAt;
    this.lastSeen = u.lastSeen;
    this.avatarImageId = u.avatarImageId;
    this.isEmailPublic = u.isEmailPublic;
    this.isPhoneNumberPublic = u.isPhoneNumberPublic;
    this.acceptedTosDate = u.acceptedTosDate;
    this.email = u.email;
    this.phoneNumber = u.phoneNumber;
    this.role = u.role;
    this.department = u.department;
  }

  public boolean hasPermission(String codename) {
    for (Permission permission : this.role.getPermissions()) {
      if (permission.codeName.equals(codename)) {
        return true;
      }
    }
    return false;
  }

  public User viewAs(User user) {
    User copy = new User(this);
    if (user != null && user.id.equals(this.id)) {
      return copy;
    }
    // user might be null!
    if (!this.isEmailPublic) {
      copy.email = null;
    }
    if (!this.isPhoneNumberPublic) {
      copy.phoneNumber = null;
    }

    return copy;
  }
}
