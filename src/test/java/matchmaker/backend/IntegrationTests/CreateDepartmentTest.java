package matchmaker.backend.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import matchmaker.backend.AuthInterceptor;
import matchmaker.backend.RequestBodies.CreateDepartmentFields;
import matchmaker.backend.constants.Perm;
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

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CreateDepartmentTest {

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

  @Autowired public ObjectMapper objectMapper;

  private static final Logger log = LoggerFactory.getLogger(ChallengeViewTest.class);
  @Autowired private PermissionRepository permissionRepository;

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

  public Role getExampleRole(String... permissions) {
    ArrayList<Permission> perms = new ArrayList<>();
    for (String perm : permissions) {
      perms.add(permissionRepository.findByCodeName(perm).get());
    }
    Role role = new Role();
    role.setName("Test Role");
    role.setPermissions(perms);
    return rolerepository.save(role);
  }

  public User getExampleUser(Department department, Role role) {
    User testUser = new User();
    testUser.setName("Test User");
    testUser.setEmail("email@email.email");
    testUser.setDepartment(department);
    testUser.setRole(role);
    return userRepository.save(testUser);
  }

  @Test
  @Transactional
  public void medewerkerCannotCreateDepartment() throws Exception {
    Role role = getExampleRole(Perm.CHALLENGE_READ);

    User testUser = userRepository.findById(1L).get();

    testUser.setRole(role);
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    testUser.setDepartment(testDepartment);
    userRepository.save(testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/department/create")
                .content(
                    objectMapper.writeValueAsString(
                        new CreateDepartmentFields("Test Department", 1L)))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  public void beheerderCanCreateDepartment() throws Exception {
    Role role = getExampleRole(Perm.DEPARTMENT_CREATE);

    Role medewerkerRole = getExampleRole(Perm.CHALLENGE_READ);
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);

    User testUser = userRepository.findById(1L).get();
    testUser.setRole(role);
    testUser.setDepartment(testDepartment);
    userRepository.save(testUser);

    User targetAdmin = getExampleUser(testUser.getDepartment(), medewerkerRole);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/department/create")
                .content(
                    objectMapper.writeValueAsString(
                        new CreateDepartmentFields("Test Department", targetAdmin.id)))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @Transactional
  public void beheerderCannotCreateDepartmentWithExternalUser() throws Exception {
    // target user is a user from another company
    Role role = getExampleRole(Perm.DEPARTMENT_CREATE);

    Role medewerkerRole = getExampleRole(Perm.CHALLENGE_READ);
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);

    User testUser = userRepository.findById(1L).get();
    testUser.setRole(role);
    testUser.setDepartment(testDepartment);
    userRepository.save(testUser);

    Company otherCompany = getExampleCompany();
    Department otherDepartment = getExampleDepartment(otherCompany);
    User targetAdmin = getExampleUser(otherDepartment, medewerkerRole);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/department/create")
                .content(
                    objectMapper.writeValueAsString(
                        new CreateDepartmentFields("Test Department", targetAdmin.id)))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  public void beheerderCannotCreateDepartmentWhenTargetAdminIsAlreadyBeheerder() throws Exception {
    Role role = getExampleRole(Perm.DEPARTMENT_CREATE);

    Role medewerkerRole = getExampleRole(Perm.CHALLENGE_READ);
    Role targetAlreadyBeheerderRole = getExampleRole(Perm.DEPARTMENT_CREATE);
    targetAlreadyBeheerderRole.isDepartmentAdmin = true;
    rolerepository.save(targetAlreadyBeheerderRole);
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    Department targetAdminDepartment = getExampleDepartment(testCompany);

    User testUser = userRepository.findById(1L).get();
    testUser.setRole(role);
    testUser.setDepartment(testDepartment);
    userRepository.save(testUser);

    User targetAdmin = getExampleUser(targetAdminDepartment, targetAlreadyBeheerderRole);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/department/create")
                .content(
                    objectMapper.writeValueAsString(
                        new CreateDepartmentFields("Test Department", targetAdmin.id)))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
