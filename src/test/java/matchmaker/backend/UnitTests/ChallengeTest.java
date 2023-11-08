package matchmaker.backend.UnitTests;

import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.constants.ChallengeVisibility;
import matchmaker.backend.models.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Date;

class ChallengeTest {

    public User testUser = new User(){
        {
            this.id = 1L;
            this.name = "Test User";
            this.info = "This is a test user.";
            this.tags = "Test, User, Test User";
            this.createdAt = new Date();
            this.lastSeen = new Date();
            this.avatarImageId = 1L;
            this.isEmailPublic = true;
            this.isPhoneNumberPublic = true;
            this.acceptedTosDate = new Date();
            this.email = "";
            this.role = testRole;
        }
    };

    public Company testCompany = new Company(){
        {
            this.id = 1L;
            this.name = "Test Company";
            this.bannerImageId = 1L;
            this.tags = "Test, Company, Test Company";
            this.createdAt = new Date();
            this.branch = "Test Branch";
        }
    };

    public Department testDepartment = new Department(){
        {
            this.id = 1L;
            this.name = "Test Department";
            this.createdAt = new Date();
            this.parentCompany = testCompany;
        }
    };

    public Challenge testChallenge = new Challenge(){
        {
            this.id = 1L;
            this.author = testUser;
            this.department = testDepartment;
            this.company = testCompany;
            this.contactInformation = "Contact information for the test challenge.";
            this.title = "New test challenge";
            this.description = "New test description.";
            this.bannerImageId = 1L;
            this.concludingRemarks = "These are some concluding remarks for the test challenge.";
            this.summary = "This is a summary of the test challenge.";
            this.status = ChallengeStatus.OPEN_VOOR_IDEEEN;
            this.createdAt = new Date();
            this.endDate = new Date();
            this.tags = "AI, Machine Learning, Prototype, Test";
            this.imageAttachmentsIds = new ArrayList<>();
            this.visibility = ChallengeVisibility.PUBLIC;
        }
    };

    public Role testRole = new Role(){
        {
            this.id = 1L;
            this.name = "Test Role";
            this.createdAt = new Date();
            this.isMatchmaker = true;
            this.company = testCompany;
            this.department = testDepartment;
        }
    };

    @Test
    void getId() {
        Long id = testChallenge.getId();
        assert(id == 1L);
    }

    @Test
    void getAuthor() {
        User author = testChallenge.getAuthor();
        assert(author == testUser);
    }

    @Test
    void getDepartment() {
        Department department = testChallenge.getDepartment();
        assert(department == testDepartment);
    }

    @Test
    void getCompany() {
        Company company = testChallenge.getCompany();
        assert(company == testCompany);
    }

    @Test
    void getContactInformation() {
        String contactInformation = testChallenge.getContactInformation();
        assert(contactInformation.equals("Contact information for the test challenge."));
    }

    @Test
    void getTitle() {
        String title = testChallenge.getTitle();
        assert(title.equals("New test challenge"));
    }

    @Test
    void getDescription() {
        String description = testChallenge.getDescription();
        assert(description.equals("New test description."));
    }

    @Test
    void getBannerImageId() {
        Long bannerImageId = testChallenge.getBannerImageId();
        assert(bannerImageId == 1L);
    }

    @Test
    void getConcludingRemarks() {
        String concludingRemarks = testChallenge.getConcludingRemarks();
        assert(concludingRemarks.equals("These are some concluding remarks for the test challenge."));
    }

    @Test
    void getSummary() {
        String summary = testChallenge.getSummary();
        assert(summary.equals("This is a summary of the test challenge."));
    }

    @Test
    void getStatus() {
        ChallengeStatus status = testChallenge.getStatus();
        assert(status == ChallengeStatus.OPEN_VOOR_IDEEEN);
    }

    @Test
    void getCreatedAt() {
        Date createdAt = testChallenge.getCreatedAt();
        assert(createdAt != null);
    }

    @Test
    void getEndDate() {
        Date endDate = testChallenge.getEndDate();
        assert(endDate != null);
    }

    @Test
    void getTags() {
        String tags = testChallenge.getTags();
        assert(tags.equals("AI, Machine Learning, Prototype, Test"));
    }

    @Test
    void getBranch() {
        String branch = testChallenge.company.getBranch();
        assert(branch.equals("Test Branch"));
    }

    @Test
    void isPublicVisible() {
        ChallengeVisibility visibility = testChallenge.getVisibility();
        assert(visibility == ChallengeVisibility.PUBLIC);
    }

    @Test
    void setId() {
        testChallenge.setId(2L);
        Long id = testChallenge.getId();
        assert(id == 2L);
    }

    @Test
    void setAuthor() {
        testChallenge.setAuthor(testUser);
        User author = testChallenge.getAuthor();
        assert(author == testUser);
    }

    @Test
    void setDepartment() {
        testChallenge.setDepartment(testDepartment);
        Department department = testChallenge.getDepartment();
        assert(department == testDepartment);
    }

    @Test
    void setCompany() {
        testChallenge.setCompany(testCompany);
        Company company = testChallenge.getCompany();
        assert(company == testCompany);
    }

    @Test
    void setContactInformation() {
        testChallenge.setContactInformation("New contact information for the test challenge.");
        String contactInformation = testChallenge.getContactInformation();
        assert(contactInformation.equals("New contact information for the test challenge."));
    }

    @Test
    void setTitle() {
        testChallenge.setTitle("New title for the test challenge.");
        String title = testChallenge.getTitle();
        assert(title.equals("New title for the test challenge."));
    }

    @Test
    void setDescription() {
        testChallenge.setDescription("New description for the test challenge.");
        String description = testChallenge.getDescription();
        assert(description.equals("New description for the test challenge."));
    }

    @Test
    void setBannerImageId() {
        testChallenge.setBannerImageId(2L);
        Long bannerImageId = testChallenge.getBannerImageId();
        assert(bannerImageId == 2L);
    }

    @Test
    void setConcludingRemarks() {
        testChallenge.setConcludingRemarks("New concluding remarks for the test challenge.");
        String concludingRemarks = testChallenge.getConcludingRemarks();
        assert(concludingRemarks.equals("New concluding remarks for the test challenge."));
    }

    @Test
    void setSummary() {
        testChallenge.setSummary("New summary for the test challenge.");
        String summary = testChallenge.getSummary();
        assert(summary.equals("New summary for the test challenge."));
    }

    @Test
    void setStatus() {
        testChallenge.setStatus(ChallengeStatus.AFGEROND);
        ChallengeStatus status = testChallenge.getStatus();
        assert(status == ChallengeStatus.AFGEROND);
    }

    @Test
    void setCreatedAt() {
        testChallenge.setCreatedAt(new Date());
        Date createdAt = testChallenge.getCreatedAt();
        assert(createdAt != null);
    }

    @Test
    void setEndDate() {
        testChallenge.setEndDate(new Date());
        Date endDate = testChallenge.getEndDate();
        assert(endDate != null);
    }

    @Test
    void setTags() {
        testChallenge.setTags("Bakker, Koffie, Thee, Brood");
        String tags = testChallenge.getTags();
        assert(tags.equals("Bakker, Koffie, Thee, Brood"));
    }

    @Test
    void setBranch() {
        testChallenge.company.setBranch("New Test Branch");
        String branch = testChallenge.company.getBranch();
        assert(branch.equals("New Test Branch"));
    }

    @Test
    void setPublicVisible() {
        testChallenge.setVisibility(ChallengeVisibility.INTERNAL);
        ChallengeVisibility visibility = testChallenge.getVisibility();
        assert(visibility == ChallengeVisibility.INTERNAL);
    }

    @Test
    void canBeSeenByPublic() {
        boolean canBeSeenBy = testChallenge.canBeSeenBy(testUser);
        assert(canBeSeenBy);
    }

    @Test
    void canNotBeSeenByInternal() {
        testChallenge.visibility = ChallengeVisibility.INTERNAL;
        boolean canBeSeenBy = testChallenge.canBeSeenBy(testUser);
        assert(!canBeSeenBy);
    }

    @Test
    void canNotBeSeenByDepartment(){
        testChallenge.visibility = ChallengeVisibility.DEPARTMENT;
        boolean canBeSeenBy = testChallenge.canBeSeenBy(testUser);
        assert(!canBeSeenBy);
    }

    @Test
    void canBeSeenByDepartment(){
        testChallenge.visibility = ChallengeVisibility.DEPARTMENT;
        testUser.role = testRole;
        boolean canBeSeenBy = testChallenge.canBeSeenBy(testUser);
        assert(canBeSeenBy);
    }

    @Test
    void canBeSeenByInternal(){
        testChallenge.visibility = ChallengeVisibility.INTERNAL;
        testUser.role = testRole;
        boolean canBeSeenBy = testChallenge.canBeSeenBy(testUser);
        assert(canBeSeenBy);
    }

    @Test
    void canBeSeenByGearchiveerdMatchMaker(){
        testChallenge.status = ChallengeStatus.GEARCHIVEERD;
        testUser.role = testRole;
        boolean canBeSeenBy = testChallenge.canBeSeenBy(testUser);
        assert(canBeSeenBy);
    }

    @Test
    void canNotBeSeenByGearchiveerd(){
        testChallenge.status = ChallengeStatus.GEARCHIVEERD;
        testUser.role = testRole;
        testUser.role.isMatchmaker = false;
        boolean canBeSeenBy = testChallenge.canBeSeenBy(testUser);
        assert(!canBeSeenBy);
    }
}