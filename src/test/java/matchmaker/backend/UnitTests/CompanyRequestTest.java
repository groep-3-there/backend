package matchmaker.backend.UnitTests;

import matchmaker.backend.models.Branch;
import matchmaker.backend.models.CompanyRequest;
import matchmaker.backend.models.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompanyRequestTest {

  @Test
  public void testGetId() {
    CompanyRequest companyRequest = new CompanyRequest();
    companyRequest.id = 1L;
    assertEquals(companyRequest.getId(), 1L);
  }

  @Test
  public void testGetName() {
    CompanyRequest companyRequest = new CompanyRequest();
    companyRequest.name = "Test Name";
    assertEquals(companyRequest.getName(), "Test Name");
  }

  @Test
  public void testGetBranch() {
    CompanyRequest companyRequest = new CompanyRequest();
    Branch branch = new Branch();
    companyRequest.branch = branch;
    assertEquals(companyRequest.getBranch(), branch);
  }

  @Test
  public void testGetTags() {
    CompanyRequest companyRequest = new CompanyRequest();
    companyRequest.tags = "Test Tags";
    assertEquals(companyRequest.getTags(), "Test Tags");
  }

  @Test
  public void testGetRequestedAt() {
    CompanyRequest companyRequest = new CompanyRequest();
    LocalDate date = LocalDate.now();
    companyRequest.requestedAt = date;
    assertEquals(companyRequest.getRequestedAt(), date);
  }

  @Test
  public void testGetOwner() {
    CompanyRequest companyRequest = new CompanyRequest();
    User user = new User();
    companyRequest.owner = user;
    assertEquals(companyRequest.getOwner(), user);
  }

  @Test
  public void testSetId() {
    CompanyRequest companyRequest = new CompanyRequest();
    companyRequest.setId(1L);
    assertEquals(companyRequest.id, 1L);
  }

  @Test
  public void testSetName() {
    CompanyRequest companyRequest = new CompanyRequest();
    companyRequest.setName("Test Name");
    assertEquals(companyRequest.name, "Test Name");
  }

  @Test
  public void testSetBranch() {
    CompanyRequest companyRequest = new CompanyRequest();
    Branch branch = new Branch();
    companyRequest.setBranch(branch);
    assertEquals(companyRequest.branch, branch);
  }

  @Test
  public void testSetTags() {
    CompanyRequest companyRequest = new CompanyRequest();
    companyRequest.setTags("Test Tags");
    assertEquals(companyRequest.tags, "Test Tags");
  }

  @Test
  public void testSetRequestedAt() {
    CompanyRequest companyRequest = new CompanyRequest();
    LocalDate date = LocalDate.now();
    companyRequest.setRequestedAt(date);
    assertEquals(companyRequest.requestedAt, date);
  }

  @Test
  public void testSetOwner() {
    CompanyRequest companyRequest = new CompanyRequest();
    User user = new User();
    companyRequest.setOwner(user);
    assertEquals(companyRequest.owner, user);
  }
}
