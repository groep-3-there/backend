package matchmaker.backend.IntegrationTests;

import com.rometools.utils.Lists;
import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.constants.ChallengeReactionType;
import matchmaker.backend.constants.Perm;
import matchmaker.backend.models.*;
import matchmaker.backend.repositories.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class GraphDataIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private CompanyRepository companyRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private RoleRepository roleRepository;

  @Autowired private DepartmentRepository departmentRepository;

  @Autowired private ChallengeRepository challengeRepository;

  @Autowired private ChallengeInputRepository challengeInputRepository;

  @Autowired private CompanyRequestRepository companyRequestRepository;

  @Autowired private PermissionRepository permissionRepository;

  private void setup() {
    Optional<Role> matchmaker = roleRepository.findById(5L);

    User user = new User("Graph data User");
    user.setCreatedAt(java.time.LocalDate.now());
    user.setRole(matchmaker.get());
    user = userRepository.save(user);

    Company company = new Company("Graph data Company");
    company.setOwnerId(user.getId());
    company.setCreatedAt(java.time.LocalDate.now().minusMonths(3));
    company = companyRepository.save(company);

    Department department = new Department("Graph data Department", company);
    department.setCreatedAt(java.time.LocalDate.now().minusMonths(3));
    department = departmentRepository.save(department);

    challengeRepository.deleteAll();
    Challenge challenge1 = new Challenge("Graph data Challenge 1");
    challenge1.setAuthor(user);
    challenge1.setDepartment(department);
    challenge1.setStatus(ChallengeStatus.OPEN_VOOR_IDEEEN);
    challenge1.setCreatedAt(java.time.LocalDate.now());
    challenge1 = challengeRepository.save(challenge1);

    Challenge challenge2 = new Challenge("Graph data Challenge 2");
    challenge2.setAuthor(user);
    challenge2.setDepartment(department);
    challenge2.setStatus(ChallengeStatus.IN_UITVOERING);
    challenge2.setCreatedAt(java.time.LocalDate.now());
    challengeRepository.save(challenge2);

    Challenge challenge3 = new Challenge("Graph data Challenge 3");
    challenge3.setAuthor(user);
    challenge3.setDepartment(department);
    challenge3.setStatus(ChallengeStatus.AFGEROND);
    challenge3.setCreatedAt(java.time.LocalDate.now().minusMonths(2));
    challengeRepository.save(challenge3);

    Challenge challenge4 = new Challenge("Graph data Challenge 4");
    challenge4.setAuthor(user);
    challenge4.setDepartment(department);
    challenge4.setStatus(ChallengeStatus.GEARCHIVEERD);
    challenge4.setCreatedAt(java.time.LocalDate.now().minusMonths(2));
    challengeRepository.save(challenge4);

    ChallengeInput challenge1Idea = new ChallengeInput();
    challenge1Idea.setText("Graph data Challenge 1 Idea");
    challenge1Idea.setChallenge(challenge1);
    challenge1Idea.setAuthor(user);
    challenge1Idea.setType(ChallengeReactionType.IDEA);
    challenge1Idea.setCreatedAt(java.time.LocalDate.now());
    challengeInputRepository.save(challenge1Idea);

    ChallengeInput challenge1Question = new ChallengeInput();
    challenge1Question.setText("Graph data Challenge 1 Question");
    challenge1Question.setChallenge(challenge1);
    challenge1Question.setAuthor(user);
    challenge1Question.setType(ChallengeReactionType.QUESTION);
    challenge1Question.setCreatedAt(java.time.LocalDate.now());
    challengeInputRepository.save(challenge1Question);

    ChallengeInput challenge1Feedback = new ChallengeInput();
    challenge1Feedback.setText("Graph data Challenge 1 Idea");
    challenge1Feedback.setChallenge(challenge1);
    challenge1Feedback.setAuthor(user);
    challenge1Feedback.setType(ChallengeReactionType.FEEDBACK);
    challenge1Feedback.setCreatedAt(java.time.LocalDate.now());
    challengeInputRepository.save(challenge1Feedback);

    ChallengeInput challenge3Idea = new ChallengeInput();
    challenge3Idea.setText("Graph data Challenge 3 Idea");
    challenge3Idea.setChallenge(challenge3);
    challenge3Idea.setAuthor(user);
    challenge3Idea.setType(ChallengeReactionType.IDEA);
    challenge3Idea.setCreatedAt(java.time.LocalDate.now().minusMonths(1));
    challenge3Idea.setChosenAnswer(true);
    challengeInputRepository.save(challenge3Idea);

    ChallengeInput challenge4Idea = new ChallengeInput();
    challenge4Idea.setText("Graph data Challenge 4 Idea");
    challenge4Idea.setChallenge(challenge3);
    challenge4Idea.setAuthor(user);
    challenge4Idea.setType(ChallengeReactionType.IDEA);
    challenge4Idea.setCreatedAt(java.time.LocalDate.now().minusMonths(1));
    challenge4Idea.setChosenAnswer(true);
    challengeInputRepository.save(challenge4Idea);

    User userForCompanyRequest = new User("Graph data User for Company Request");
    userForCompanyRequest.setCreatedAt(java.time.LocalDate.now());
    userForCompanyRequest.setRole(matchmaker.get());
    userForCompanyRequest = userRepository.save(userForCompanyRequest);

    companyRequestRepository.deleteAll();
    CompanyRequest companyRequest = new CompanyRequest();
    companyRequest.setRequestedAt(LocalDate.now());
    companyRequest.setName("Graph data Company Request");
    companyRequest.setOwner(userForCompanyRequest);
    companyRequestRepository.save(companyRequest);
  }

  private void teardown() {
    userRepository.deleteAll();
    companyRepository.deleteAll();
    departmentRepository.deleteAll();
    challengeRepository.deleteAll();
    challengeInputRepository.deleteAll();
  }

  @Test
  public void testGetTotalChallenges() throws Exception {
    setup();

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/graph-data/challenges/total")
                .contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              String response = result.getResponse().getContentAsString();
              assert response.contains("4");
            });

    //        teardown();
  }

  @Test
  public void testGetTotalChallengesByDate() throws Exception {
    setup();

    LocalDate now = LocalDate.now();
    String from = now.minusMonths(3).toString();
    String till = now.toString();

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/graph-data/challenges/total-by-date" + "?from=" + from + "&till=" + till)
                .contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              String response = result.getResponse().getContentAsString();
              assert response.contains(now.getMonth().name() + "-" + now.getYear() + "\":4");
            });
  }

  @Test
  public void testGetTotalChallengesByStatus() throws Exception {
    setup();

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/graph-data/challenges/status")
                .contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              String response = result.getResponse().getContentAsString();
              assert response.contains(
                  "{\"OPEN_VOOR_IDEEEN\":1,\"IN_UITVOERING\":1,\"AFGEROND\":1,\"GEARCHIVEERD\":1}");
            });
  }

  @Test
  public void testGetChallengesForRangeOfMonthsFilter() throws Exception {
    setup();

    LocalDate now = LocalDate.now();
    String from = now.minusMonths(1).toString();
    String till = now.toString();

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/graph-data/challenges/filter/date?from=" + from + "&till=" + till)
                .contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              String response = result.getResponse().getContentAsString();
              assert response.contains(now.getMonth().name() + "-" + now.getYear() + "\":2");
            });
  }

  @Test
  public void testGetTotalUsers() throws Exception {
    setup();

    Long count = userRepository.count();

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/graph-data/users/total").contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              String response = result.getResponse().getContentAsString();
              assert response.contains(count.toString());
            });
  }

  @Test
  public void testGetTotalCompanies() throws Exception {
    setup();

    Long count = companyRepository.count();

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/graph-data/companies/total")
                .contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              String response = result.getResponse().getContentAsString();
              assert response.contains(count.toString());
            });
  }

  @Test
  public void testGetTotalCompanyRequests() throws Exception {
    setup();

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/graph-data/company-requests/total")
                .contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              String response = result.getResponse().getContentAsString();
              assert response.contains("1");
            });
  }

  @Test
  public void testGetCompaniesForRangeOfMonthsFilter() throws Exception {
    setup();

    LocalDate now = LocalDate.now();
    String from = now.minusMonths(3).toString();
    String till = now.toString();

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/graph-data/companies/filter/date?from=" + from + "&till=" + till)
                .contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              String response = result.getResponse().getContentAsString();
              assert response.contains(
                  now.minusMonths(3).getMonth().name() + "-" + now.getYear() + "\":1");
            });
  }

  @Test
  public void testGetTotalCompaniesForEmptyUser() throws Exception {
    setup();

    MockitoAnnotations.initMocks(this);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/graph-data/companies/total")
                .contentType("application/json")
                .param("loggedOut", "true"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  public void testgetChallengesForRangeOfMonthsFilterAndCompanyId() throws Exception {
    setup();

    User user = userRepository.findById(1L).get();

    Company company = (Company) companyRepository.findByName("Graph data Company").get(0);
    Iterable<Department> departments = departmentRepository.findAllByParentCompanyId(company.id);

    departments.forEach(dep -> user.department = dep);
    userRepository.save(user);

    LocalDate now = LocalDate.now();
    String from = now.minusMonths(3).toString();
    String till = now.toString();

    mockMvc
            .perform(
                    MockMvcRequestBuilders.get(
                                    "/graph-data/company/" + company.getId() + "/challenges/filter/date" + "?from=" + from + "&till=" + till)
                            .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(
                    result -> {
                      String response = result.getResponse().getContentAsString();
                      assert response.contains(now.minusMonths(3).getMonth().name() + "-" + now.getYear() + "\":2");
                    });
  }

  @Test
  @Transactional
  public void testgetChallengesByStatusCountAndCompanyId() throws Exception {
    setup();

    User user = userRepository.findById(1L).get();

    Company company = (Company) companyRepository.findByName("Graph data Company").get(0);
    Iterable<Department> departments = departmentRepository.findAllByParentCompanyId(company.id);

    departments.forEach(dep -> user.department = dep);
    userRepository.save(user);

    mockMvc
            .perform(
                    MockMvcRequestBuilders.get(
                                    "/graph-data/company/" + company.getId() + "/challenges/status")
                            .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(
                    result -> {
                      String response = result.getResponse().getContentAsString();
                      assert response.contains("{\"OPEN_VOOR_IDEEEN\":1,\"IN_UITVOERING\":1,\"AFGEROND\":1,\"GEARCHIVEERD\":1}");
                    });
  }

  @Test
  @Transactional
  public void getUsersByDepartmentsAndCompanyId() throws Exception {
    setup();

    User user = userRepository.findById(1L).get();

    Company company = (Company) companyRepository.findByName("Graph data Company").get(0);
    Iterable<Department> departments = departmentRepository.findAllByParentCompanyId(company.id);

    departments.forEach(dep -> user.department = dep);
    userRepository.save(user);

    mockMvc
            .perform(
                    MockMvcRequestBuilders.get(
                                    "/graph-data/company/" + company.getId() + "/departments/users")
                            .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(
                    result -> {
                      String response = result.getResponse().getContentAsString();
                      assert response.contains("{\"Graph data Department\":1}");
                    });
  }
  @Test
  @Transactional
  public void getUsersByDepartmentsAndCompanyIdWhenNoPermission() throws Exception {
    setup();

    User user = userRepository.findById(1L).get();
    user.role.permissions = new ArrayList<>();
    user.role.isMatchmaker = false;

    Company company = (Company) companyRepository.findByName("Graph data Company").get(0);
    Iterable<Department> departments = departmentRepository.findAllByParentCompanyId(company.id);

    departments.forEach(dep -> user.department = dep);
    userRepository.save(user);

    mockMvc
            .perform(
                    MockMvcRequestBuilders.get(
                                    "/graph-data/company/" + company.getId() + "/departments/users")
                            .contentType("application/json"))
            .andExpect(status().isUnauthorized());

  }
}
