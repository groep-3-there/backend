package matchmaker.backend.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import matchmaker.backend.AuthInterceptor;
import matchmaker.backend.constants.DefaultRoleId;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DepartmentIntegrationTest {
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
  @Autowired public PermissionRepository permissionRepository;

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
  public void getDepartment() throws Exception {
    Role role = getExampleRole(Perm.CHALLENGE_READ);
    User testUser = userRepository.findById(1L).get();

    testUser.setRole(role);
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    testUser.setDepartment(testDepartment);
    userRepository.save(testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/department/" + testDepartment.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @Transactional
  public void getDepartmentLoggedOut() throws Exception {
    Role role = getExampleRole(Perm.CHALLENGE_READ);
    User testUser = userRepository.findById(1L).get();

    testUser.setRole(role);
    Company testCompany = getExampleCompany();
    Department testDepartment = getExampleDepartment(testCompany);
    testUser.setDepartment(testDepartment);
    userRepository.save(testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/department/" + testDepartment.getId().toString() + "?loggedOut=1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @Transactional
  public void getCompanyDepartments() throws Exception {
    Role role = getExampleRole(Perm.CHALLENGE_READ);
    User testUser = userRepository.findById(1L).get();

    testUser.setRole(role);
    Company testCompany = getExampleCompany();

    Department testDepartment = getExampleDepartment(testCompany);
    Department testDepartment2 = getExampleDepartment(testCompany);
    testUser.setDepartment(testDepartment);
    userRepository.save(testUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/department/company/" + testCompany.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").exists());
    // expect list
  }

  @Test
  public void testUpdateRoles() throws Exception {
    Role role = rolerepository.findById(DefaultRoleId.DEPARTMENT_BEHEERDER).get();
    User testUser = userRepository.findById(1L).get();
    testUser.setRole(role);

    Company testCompany = getExampleCompany();
    companyRepository.save(testCompany);
    Department testDepartment = getExampleDepartment(testCompany);
    departmentRepository.save(testDepartment);
    testUser.setDepartment(testDepartment);
    userRepository.save(testUser);

    User testUser2 = getExampleUser(testDepartment, role);
    userRepository.save(testUser2);


    Map<String, List<Map<String, Long>>> requestMap = Map.of("updates", List.of(Map.of("userId", testUser2.id, "roleId", 2L)));

    mockMvc.perform(MockMvcRequestBuilders.put("/department/" + testUser.department.id + "/updateroles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestMap)))
            .andExpect(status().isOk());
  }

  @Test
  public void testGetDepartmentMembers() throws Exception{
    Role role = rolerepository.findById(DefaultRoleId.DEPARTMENT_BEHEERDER).get();
    User testUser = userRepository.findById(1L).get();
    testUser.setRole(role);

    Company testCompany = getExampleCompany();
    companyRepository.save(testCompany);
    Department testDepartment = getExampleDepartment(testCompany);
    departmentRepository.save(testDepartment);
    testUser.setDepartment(testDepartment);
    userRepository.save(testUser);

    User testUser2 = getExampleUser(testDepartment, role);
    userRepository.save(testUser2);

    mockMvc.perform(MockMvcRequestBuilders.get("/department/" + testUser.department.id + "/members")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").exists());

  }
}
