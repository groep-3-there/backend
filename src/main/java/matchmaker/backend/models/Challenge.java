package matchmaker.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.constants.ChallengeVisibility;
import matchmaker.backend.constants.Perm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "challenges")
@EnableAutoConfiguration
@AllArgsConstructor
@Getter
@Setter
public class Challenge {
  public Challenge() {}

  @Transient private static final Logger log = LoggerFactory.getLogger(Challenge.class);

  public Challenge(String title) {
    this.title = title;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "challenge_id")
  @TableGenerator(name = "challenge_id", initialValue = 1000)
  public Long id;

  @ManyToOne public User author;

  @ManyToOne public Department department;

  @Column(length = 65535, columnDefinition = "Text")
  public String contactInformation;

  public String title;

  @Column(length = 65535, columnDefinition = "Text")
  public String description;

  public Long bannerImageId;
  public String concludingRemarks;

  @Column(length = 65535, columnDefinition = "Text")
  public String summary;

  @Enumerated(EnumType.ORDINAL)
  public ChallengeStatus status;

  public LocalDate createdAt;
  public LocalDate endDate;
  public String tags;

  @ElementCollection(fetch = FetchType.EAGER)
  public List<Long> imageAttachmentsIds;

  @Enumerated(EnumType.ORDINAL)
  public ChallengeVisibility visibility;

  public boolean canBeSeenBy(User user) {
    // Opties zonder account
    if (user == null) {
      if (this.visibility == ChallengeVisibility.PUBLIC
          && this.status != ChallengeStatus.GEARCHIVEERD) {
        return true;
      }
      return false;
    }
    // You need an account from here on

    // Status = OPEN_VOOR_IDEEEN || AFGEROND || IN_UITVOERING

    if (this.visibility == ChallengeVisibility.PUBLIC
        && this.status != ChallengeStatus.GEARCHIVEERD) {
      return true;
    }
    if (this.visibility == ChallengeVisibility.INTRANET
        && this.status != ChallengeStatus.GEARCHIVEERD) {
      return true; // We already know the user is logged in
    }

    if (!user.isInCompany()) {
      return false;
    } // If user is not in a company, abort
    Long userCompanyId = user.department.parentCompany.id;
    if (!this.department.parentCompany.id.equals(userCompanyId)) {
      return false;
    } // if user is not in the same company as the challenge, abort
    // User is part of same company as the challenge

    // If archived, only managers can view the challenge
    if (status == ChallengeStatus.GEARCHIVEERD) {
      return user.hasPermissionAtDepartment(Perm.CHALLENGE_MANAGE, user.department.id);
    }

    if (this.visibility == ChallengeVisibility.INTERNAL) {
      return true;
    }

    if (this.visibility == ChallengeVisibility.DEPARTMENT) {
      // If the user is in the same department
      return user.department.id.equals(this.department.id);
    }

    log.warn(
        "Challenge and/or user did not fit any permission conditions, some checks are missing!");
    return false;
  }

  public boolean canBeEditedBy(User user) {
    if (!user.isInCompany()) {
      return false;
    }
    boolean userPartOfDepartmentChallenge =
        this.department.id.equals(
            user.department.id); // is user in the same department as the challenge
    return userPartOfDepartmentChallenge
        && user.hasPermissionAtDepartment(Perm.CHALLENGE_MANAGE, user.department.id);
  }
}
