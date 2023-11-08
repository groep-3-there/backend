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

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "challenges")
@EnableAutoConfiguration
@AllArgsConstructor
@Getter
@Setter
public class Challenge {
    public Challenge(){

    }

    @Transient
    private static final Logger log = LoggerFactory.getLogger(Challenge.class);


    public Challenge(String title, User author){
        this.author = author;
        this.title = title;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @ManyToOne
    public User author;

    @ManyToOne
    public Department department;

    @ManyToOne
    public Company company;

    public String contactInformation;
    public String title;
    public String description;
    public Long bannerImageId;
    public String concludingRemarks;
    public String summary;

    @Enumerated(EnumType.ORDINAL)
    public ChallengeStatus status;
    public Date createdAt;
    public Date endDate;
    public String tags;

    @ElementCollection
    public List<Long> imageAttachmentsIds;

    @Enumerated(EnumType.ORDINAL)
    public ChallengeVisibility visibility;


    public boolean canBeSeenBy(User user){
        //Opties zonder account
        if(user.equals(null)){
            if(this.visibility == ChallengeVisibility.PUBLIC && this.status != ChallengeStatus.GEARCHIVEERD){
                return true;
            }
            return false;
        }
        //You need an account from here on

        // Status = OPEN_VOOR_IDEEEN || AFGEROND || IN_UITVOERING

        if(this.visibility == ChallengeVisibility.PUBLIC && this.status != ChallengeStatus.GEARCHIVEERD){
            return true;
        }
        if(this.visibility == ChallengeVisibility.INTRANET && this.status != ChallengeStatus.GEARCHIVEERD){
            return true; //We already know the user is logged in
        }


        if(!user.isInCompany()){ return false; } // If user is not in a company, abort
        Long companyId = user.role.company.id;

        if(!this.company.id.equals(companyId)){ return false; } // if user is not in the same company as the challenge, abort
        //User is part of same company as the challenge

        //If archived, only managers can view the challenge
        if(status == ChallengeStatus.GEARCHIVEERD){
            return user.hasPermissionAtCompany(Perm.CHALLENGE_MANAGE, companyId);
        }

        if(this.visibility == ChallengeVisibility.INTERNAL){
            return true;
        }

        if(this.visibility == ChallengeVisibility.DEPARTMENT){
            //If the user is in the same department
            return user.role.department.id.equals(this.department.id);
        }

        log.warn("Challenge and/or user did not fit any permission conditions, some checks are missing!");
        return false;
    }


    public boolean canBeEditedBy(User user){
        if(!user.isInCompany()) { return false; }
        boolean userPartOfCompanyChallenge = this.company.id.equals(user.role.company.id); // is user in the same company as the challenge
        return userPartOfCompanyChallenge && user.hasPermissionAtCompany(Perm.CHALLENGE_MANAGE, user.role.company.id);
    }


}
