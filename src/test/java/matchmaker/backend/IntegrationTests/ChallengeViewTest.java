package matchmaker.backend.IntegrationTests;

import com.google.firebase.FirebaseApp;
import matchmaker.backend.AuthInterceptor;
import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.constants.ChallengeVisibility;
import matchmaker.backend.constants.DefaultRoleId;
import matchmaker.backend.controllers.ChallengeController;
import matchmaker.backend.controllers.UserController;
import matchmaker.backend.models.*;
import matchmaker.backend.repositories.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ChallengeViewTest {

  @Autowired private MockMvc mockMvc;

  @Autowired public ChallengeRepository challengeRepository;
  @Autowired public CompanyRepository companyRepository;
  @Autowired public DepartmentRepository departmentRepository;
  @Autowired public UserRepository userRepository;
  @Autowired public RoleRepository rolerepository;
  @Autowired public BranchRepository branchRepository;
  @Autowired private UserController userController;

  @Autowired private ChallengeController challengeController;
  @InjectMocks private AuthInterceptor authInterceptor;

  @Mock private FirebaseApp firebaseApp;

  private static final Logger log = LoggerFactory.getLogger(ChallengeViewTest.class);

  public Company getExampleCompany() {
    Branch testBranch = branchRepository.findById(1L).get();
    Company testCompany = new Company();
    testCompany.setName("Philips");
    testCompany.setBranch(testBranch);
    testCompany.setTags("Website");
    testCompany.ownerId = 1L;
    testCompany.setInfo("Dit is het bedrijf Philips");
    return companyRepository.save(testCompany);
  }

  public Department getExampleDepartment(Company parent) {
    Department testDepartment = new Department();
    testDepartment.setName("ICT");
    testDepartment.setParentCompany(parent);
    return departmentRepository.save(testDepartment);
  }

  public Challenge getExampleChallenge(
      ChallengeStatus status, ChallengeVisibility visibility, Department department, User author) {

    Challenge testChallenge = new Challenge();
    testChallenge.setTitle("Zuivere Koffie");
    testChallenge.setDescription(
        "Ik wil graag mijn bedrijf innoveren. Ik zoek een team van 3 personen die mij hierbij"
            + " kunnen helpen.");
    testChallenge.setCreatedAt(new java.util.Date());
    testChallenge.setEndDate(new java.util.Date());
    testChallenge.setSummary("Bedrijf Innoveren");
    testChallenge.setDepartment(department);
    testChallenge.setStatus(status);
    testChallenge.setVisibility(visibility);
    testChallenge.setAuthor(author);
    testChallenge.setTags("Website");
    return challengeRepository.save(testChallenge);
  }

  @Test
  @Transactional
  public void medewerkerGetChallengePublicOpenVoorIdeeen() throws Exception {
    Role role = rolerepository.findById(1L).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    testUser.setDepartment(testDepartment);
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.OPEN_VOOR_IDEEEN, ChallengeVisibility.PUBLIC, testDepartment, testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @Transactional
  public void medewerkerGetChallengePublicInUitvoering() throws Exception {
    Role role = rolerepository.findById(1L).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    testUser.setDepartment(testDepartment);
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.IN_UITVOERING, ChallengeVisibility.PUBLIC, testDepartment, testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @Transactional
  public void medewerkerGetChallengePublicAfgerond() throws Exception {
    Role role = rolerepository.findById(1L).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    testUser.setDepartment(testDepartment);
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.AFGEROND, ChallengeVisibility.PUBLIC, testDepartment, testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @Transactional
  public void medewerkerCannotGetChallengePublicGearchiveerd() throws Exception {
    Role role = rolerepository.findById(DefaultRoleId.MEDEWERKER).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    testUser.setDepartment(testDepartment);
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.GEARCHIVEERD, ChallengeVisibility.PUBLIC, testDepartment, testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  public void challengerGetChallengePublicGearchiveerd() throws Exception {
    Role role = rolerepository.findById(DefaultRoleId.CHALLENGER).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    testUser.setDepartment(testDepartment);
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.GEARCHIVEERD, ChallengeVisibility.PUBLIC, testDepartment, testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @Transactional
  public void medewerkerGetChallengePublicOpenVoorIdeenFromOtherCompany() throws Exception {
    Role role = rolerepository.findById(DefaultRoleId.CHALLENGER).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    //        testUser.setDepartment(testDepartment);   //Don't give the user the department that we
    // use for the challenge
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.OPEN_VOOR_IDEEEN, ChallengeVisibility.PUBLIC, testDepartment, testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @Transactional
  public void medewerkerGetChallengePublicInUivoeringFromOtherCompany() throws Exception {
    Role role = rolerepository.findById(DefaultRoleId.CHALLENGER).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    //        testUser.setDepartment(testDepartment);   //Don't give the user the department that we
    // use for the challenge
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.IN_UITVOERING, ChallengeVisibility.PUBLIC, testDepartment, testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @Transactional
  public void medewerkerGetChallengeIntranetInUitvoeringFromOtherCompany() throws Exception {
    Role role = rolerepository.findById(DefaultRoleId.CHALLENGER).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    //        testUser.setDepartment(testDepartment);   //Don't give the user the department that we
    // use for the challenge
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.IN_UITVOERING, ChallengeVisibility.INTRANET, testDepartment, testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @Transactional
  public void medewerkerCannotGetChallengeInternalInUitvoeringFromOtherCompany() throws Exception {
    Role role = rolerepository.findById(DefaultRoleId.CHALLENGER).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    //        testUser.setDepartment(testDepartment);   //Don't give the user the department that we
    // use for the challenge
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.IN_UITVOERING, ChallengeVisibility.INTERNAL, testDepartment, testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  public void medewerkerCannotGetChallengeDepartmentInUitvoeringFromOtherCompany()
      throws Exception {
    Role role = rolerepository.findById(DefaultRoleId.CHALLENGER).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    //        testUser.setDepartment(testDepartment);   //Don't give the user the department that we
    // use for the challenge
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.IN_UITVOERING,
            ChallengeVisibility.DEPARTMENT,
            testDepartment,
            testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  public void medewerkerGetChallengeDepartmentInUitvoeringFromSameDepartment() throws Exception {
    Role role = rolerepository.findById(DefaultRoleId.CHALLENGER).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    testUser.setDepartment(
        testDepartment); // Don't give the user the department that we use for the challenge
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.IN_UITVOERING,
            ChallengeVisibility.DEPARTMENT,
            testDepartment,
            testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @Transactional
  public void medewerkerGetChallengeDepartmentInUitvoeringFromDifferentDepartment()
      throws Exception {
    Role role = rolerepository.findById(DefaultRoleId.CHALLENGER).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    Department testDepartment2 = getExampleDepartment(testCompany);
    testUser.setDepartment(
        testDepartment2); // Don't give the user the department that we use for the challenge
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.IN_UITVOERING,
            ChallengeVisibility.DEPARTMENT,
            testDepartment,
            testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  public void loggedOutGetChallengePublicInUitvoering() throws Exception {
    Role role = rolerepository.findById(DefaultRoleId.CHALLENGER).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    Department testDepartment2 = getExampleDepartment(testCompany);
    testUser.setDepartment(
        testDepartment2); // Don't give the user the department that we use for the challenge
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.IN_UITVOERING, ChallengeVisibility.PUBLIC, testDepartment, testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id + "?loggedOut=1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @Transactional
  public void loggedOutGetChallengeDepartmentInUitvoering() throws Exception {
    Role role = rolerepository.findById(DefaultRoleId.CHALLENGER).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    Department testDepartment2 = getExampleDepartment(testCompany);
    testUser.setDepartment(
        testDepartment2); // Don't give the user the department that we use for the challenge
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.IN_UITVOERING,
            ChallengeVisibility.DEPARTMENT,
            testDepartment,
            testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id + "?loggedOut=1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  public void loggedOutGetChallengeInternalInUitvoering() throws Exception {
    Role role = rolerepository.findById(DefaultRoleId.CHALLENGER).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    Department testDepartment2 = getExampleDepartment(testCompany);
    testUser.setDepartment(
        testDepartment2); // Don't give the user the department that we use for the challenge
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.IN_UITVOERING, ChallengeVisibility.INTERNAL, testDepartment, testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id + "?loggedOut=1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  public void loggedOutGetChallengeIntranetInUitvoering() throws Exception {
    Role role = rolerepository.findById(DefaultRoleId.CHALLENGER).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    Department testDepartment2 = getExampleDepartment(testCompany);
    testUser.setDepartment(
        testDepartment2); // Don't give the user the department that we use for the challenge
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.IN_UITVOERING, ChallengeVisibility.INTRANET, testDepartment, testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id + "?loggedOut=1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  public void loggedOutGetChallengePublicArchived() throws Exception {
    Role role = rolerepository.findById(DefaultRoleId.CHALLENGER).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    Department testDepartment2 = getExampleDepartment(testCompany);
    testUser.setDepartment(
        testDepartment2); // Don't give the user the department that we use for the challenge
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.GEARCHIVEERD, ChallengeVisibility.PUBLIC, testDepartment, testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id + "?loggedOut=1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  public void loggedOutGetChallengeInternalArchived() throws Exception {
    Role role = rolerepository.findById(DefaultRoleId.CHALLENGER).get();
    User testUser = userRepository.findById(1L).get();
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    Department testDepartment2 = getExampleDepartment(testCompany);
    testUser.setDepartment(
        testDepartment2); // Don't give the user the department that we use for the challenge
    testUser.setRole(role);
    userRepository.save(testUser);

    Challenge testChallenge =
        getExampleChallenge(
            ChallengeStatus.GEARCHIVEERD, ChallengeVisibility.INTERNAL, testDepartment, testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/challenge/" + testChallenge.id + "?loggedOut=1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }
}
