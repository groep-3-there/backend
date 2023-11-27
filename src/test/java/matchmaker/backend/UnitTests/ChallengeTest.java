package matchmaker.backend.UnitTests;

import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.constants.ChallengeVisibility;
import matchmaker.backend.models.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeTest {

  @Test
  void canBeSeenByPublicUser() {
    Challenge testChallenge = new Challenge();
    User testUser = new User("testUser");
    testChallenge.visibility = ChallengeVisibility.PUBLIC;
    assertEquals(testChallenge.canBeSeenBy(testUser), true);
  }

  @Test
  void canBeSeenByIntranetUser() {
    Challenge testChallenge = new Challenge();
    User testUser = new User("testUser");
    testChallenge.visibility = ChallengeVisibility.INTRANET;
    assertEquals(testChallenge.canBeSeenBy(testUser), true);
  }

  @Test
  void canBeSeenByInternalUser() {
    Challenge testChallenge = new Challenge();
    User testUser = new User("testUser");
    Company testCompany = new Company();
    testCompany.setId(1L);
    Department testDepartment = new Department();
    testDepartment.setId(1L);
    testDepartment.setParentCompany(testCompany);
    testUser.department = testDepartment;
    testChallenge.setDepartment(testDepartment);
    testChallenge.visibility = ChallengeVisibility.INTERNAL;
    assertEquals(testChallenge.canBeSeenBy(testUser), true);
  }

  @Test
  void canBeSeenByDepartmentUser() {
    Challenge testChallenge = new Challenge();
    User testUser = new User("testUser");
    Company testCompany = new Company();
    testCompany.setId(1L);
    Department testDepartment = new Department();
    testDepartment.id = 1L;
    testDepartment.parentCompany = testCompany;
    testUser.department = testDepartment;
    testChallenge.department = testDepartment;
    testChallenge.visibility = ChallengeVisibility.DEPARTMENT;
    assertEquals(testChallenge.canBeSeenBy(testUser), true);
  }

  @Test
  void canBeEditedByMatchmaker() {
    Challenge testChallenge = new Challenge();
    User testUser = new User("testUser");
    Department testDepartment = new Department();
    testDepartment.id = 1L;
    Role testrole = new Role("testRole");
    testrole.isMatchmaker = true;

    testChallenge.author = testUser;
    testChallenge.department = testDepartment;
    testUser.department = testDepartment;
    testUser.role = testrole;

    assertEquals(testChallenge.canBeEditedBy(testUser), true);
  }

  @Test
  void canBeEditedByDepartment() {
    Challenge testChallenge = new Challenge();
    User testUser = new User("testUser");
    Department testDepartment = new Department();
    testDepartment.id = 1L;
    Role testrole = new Role("testRole");
    testrole.isMatchmaker = false;
    List<Permission> testList = new ArrayList<>();
    testList.add(new Permission("CHALLENGE_MANAGE", "testDescription", "testFancyName"));
    testrole.setPermissions(testList);

    testChallenge.author = testUser;
    testChallenge.department = testDepartment;
    testUser.department = testDepartment;
    testUser.role = testrole;

    assertEquals(testChallenge.canBeEditedBy(testUser), true);
  }

  @Test
  void getId() {
    Challenge testChallenge = new Challenge();
    testChallenge.id = 1L;
    assertEquals(testChallenge.getId(), 1L);
  }

  @Test
  void getAuthor() {
    Challenge testChallenge = new Challenge();
    User testUser = new User("testUser");
    testChallenge.author = testUser;
    assertEquals(testChallenge.getAuthor(), testUser);
  }

  @Test
  void getDepartment() {
    Challenge testChallenge = new Challenge();
    Department testDepartment = new Department();
    testChallenge.department = testDepartment;
    assertEquals(testChallenge.getDepartment(), testDepartment);
  }

  @Test
  void getContactInformation() {
    Challenge testChallenge = new Challenge();
    testChallenge.contactInformation = "testContactInformation";
    assertEquals(testChallenge.getContactInformation(), "testContactInformation");
  }

  @Test
  void getTitle() {
    Challenge testChallenge = new Challenge();
    testChallenge.title = "testTitle";
    assertEquals(testChallenge.getTitle(), "testTitle");
  }

  @Test
  void getDescription() {
    Challenge testChallenge = new Challenge();
    testChallenge.description = "testDescription";
    assertEquals(testChallenge.getDescription(), "testDescription");
  }

  @Test
  void getBannerImageId() {
    Challenge testChallenge = new Challenge();
    testChallenge.bannerImageId = 1L;
    assertEquals(testChallenge.getBannerImageId(), 1L);
  }

  @Test
  void getConcludingRemarks() {
    Challenge testChallenge = new Challenge();
    testChallenge.concludingRemarks = "testConcludingRemarks";
    assertEquals(testChallenge.getConcludingRemarks(), "testConcludingRemarks");
  }

  @Test
  void getSummary() {
    Challenge testChallenge = new Challenge();
    testChallenge.summary = "testSummary";
    assertEquals(testChallenge.getSummary(), "testSummary");
  }

  @Test
  void getStatus() {
    Challenge testChallenge = new Challenge();
    testChallenge.status = ChallengeStatus.OPEN_VOOR_IDEEEN;
    assertEquals(testChallenge.getStatus(), ChallengeStatus.OPEN_VOOR_IDEEEN);
  }

  @Test
  void getCreatedAt() {
    Challenge testChallenge = new Challenge();
    Date testdate = new Date();
    testChallenge.createdAt = testdate;
    assertEquals(testChallenge.getCreatedAt(), testdate);
  }

  @Test
  void getEndDate() {
    Challenge testChallenge = new Challenge();
    Date testdate = new Date();
    testChallenge.endDate = testdate;
    assertEquals(testChallenge.getEndDate(), testdate);
  }

  @Test
  void getTags() {
    Challenge testChallenge = new Challenge();
    testChallenge.tags = "testTags";
    assertEquals(testChallenge.getTags(), "testTags");
  }

  @Test
  void getImageAttachmentsIds() {
    Challenge testChallenge = new Challenge();
    List<Long> testList = new ArrayList<>();
    testList.add(1L);
    testList.add(2L);
    testChallenge.imageAttachmentsIds = testList;
    assertEquals(testChallenge.getImageAttachmentsIds(), testList);
  }

  @Test
  void getVisibility() {
    Challenge testChallenge = new Challenge();
    testChallenge.visibility = ChallengeVisibility.PUBLIC;
    assertEquals(testChallenge.getVisibility(), ChallengeVisibility.PUBLIC);
  }

  @Test
  void setId() {
    Challenge testChallenge = new Challenge();
    testChallenge.setId(1L);
    assertEquals(testChallenge.id, 1L);
  }

  @Test
  void setAuthor() {
    Challenge testChallenge = new Challenge();
    User testUser = new User("testUser");
    testChallenge.setAuthor(testUser);
    assertEquals(testChallenge.author, testUser);
  }

  @Test
  void setDepartment() {
    Challenge testChallenge = new Challenge();
    Department testDepartment = new Department();
    testChallenge.setDepartment(testDepartment);
    assertEquals(testChallenge.department, testDepartment);
  }

  @Test
  void setContactInformation() {
    Challenge testChallenge = new Challenge();
    testChallenge.setContactInformation("testContactInformation");
    assertEquals(testChallenge.contactInformation, "testContactInformation");
  }

  @Test
  void setTitle() {
    Challenge testChallenge = new Challenge();
    testChallenge.setTitle("testTitle");
    assertEquals(testChallenge.title, "testTitle");
  }

  @Test
  void setDescription() {
    Challenge testChallenge = new Challenge();
    testChallenge.setDescription("testDescription");
    assertEquals(testChallenge.description, "testDescription");
  }

  @Test
  void setBannerImageId() {
    Challenge testChallenge = new Challenge();
    testChallenge.setBannerImageId(1L);
    assertEquals(testChallenge.bannerImageId, 1L);
  }

  @Test
  void setConcludingRemarks() {
    Challenge testChallenge = new Challenge();
    testChallenge.setConcludingRemarks("testConcludingRemarks");
    assertEquals(testChallenge.concludingRemarks, "testConcludingRemarks");
  }

  @Test
  void setSummary() {
    Challenge testChallenge = new Challenge();
    testChallenge.setSummary("testSummary");
    assertEquals(testChallenge.summary, "testSummary");
  }

  @Test
  void setStatus() {
    Challenge testChallenge = new Challenge();
    testChallenge.setStatus(ChallengeStatus.OPEN_VOOR_IDEEEN);
    assertEquals(testChallenge.status, ChallengeStatus.OPEN_VOOR_IDEEEN);
  }

  @Test
  void setCreatedAt() {
    Challenge testChallenge = new Challenge();
    Date testdate = new Date();
    testChallenge.setCreatedAt(testdate);
    assertEquals(testChallenge.createdAt, testdate);
  }

  @Test
  void setEndDate() {
    Challenge testChallenge = new Challenge();
    Date testdate = new Date();
    testChallenge.setEndDate(testdate);
    assertEquals(testChallenge.endDate, testdate);
  }

  @Test
  void setTags() {
    Challenge testChallenge = new Challenge();
    testChallenge.setTags("testTags");
    assertEquals(testChallenge.tags, "testTags");
  }

  @Test
  void setImageAttachmentsIds() {
    Challenge testChallenge = new Challenge();
    List<Long> testList = new ArrayList<>();
    testList.add(1L);
    testList.add(2L);
    testChallenge.setImageAttachmentsIds(testList);
    assertEquals(testChallenge.imageAttachmentsIds, testList);
  }

  @Test
  void setVisibility() {
    Challenge testChallenge = new Challenge();
    testChallenge.setVisibility(ChallengeVisibility.PUBLIC);
    assertEquals(testChallenge.visibility, ChallengeVisibility.PUBLIC);
  }
}
