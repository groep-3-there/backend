package matchmaker.backend.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import matchmaker.backend.AuthInterceptor;
import matchmaker.backend.controllers.UserController;
import matchmaker.backend.models.Company;
import matchmaker.backend.models.Department;
import matchmaker.backend.models.Role;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.CompanyRepository;
import matchmaker.backend.repositories.DepartmentRepository;
import matchmaker.backend.repositories.RoleRepository;
import matchmaker.backend.repositories.UserRepository;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private RoleRepository roleRepository;

  @Autowired private DepartmentRepository departmentRepository;

  @Autowired private CompanyRepository companyRepository;

  private static final Logger log = LoggerFactory.getLogger(CompanyIntegrationTest.class);

  // FROM HERE ONWARDS, THE CODE IS NEEDED FOR THE AUTH INTERCEPTOR TO WORK
  @InjectMocks private AuthInterceptor authInterceptor;

  @Mock private FirebaseApp firebaseApp;

  @Autowired private UserRepository userRepository;

  @Autowired private UserController userController;

  @Autowired public ObjectMapper objectMapper;

  @Test
  public void getUserById() throws Exception {
    Optional<Role> role = roleRepository.findById(1L);
    Department department = new Department();
    department.setName("Test department");
    Company company = new Company();
    company.setName("Test company");
    companyRepository.save(company);
    department.setParentCompany(company);
    departmentRepository.save(department);
    // Make a new user in the repository
    User testUser = new User();
    testUser.name = "Jan Bakker";
    testUser.email = "jan.bakker@mail.com";
    testUser.acceptedTosDate = LocalDate.now();
    testUser.avatarImageId = 1L;
    testUser.createdAt = LocalDate.now();
    testUser.department = null;
    testUser.firebaseId = "3WUKhR2EcvQkwP6R5R4ZOudrJQO2";
    testUser.info = "Jan Bakker bakt graag bij bakker bart.";
    testUser.isEmailPublic = true;
    testUser.isPhoneNumberPublic = true;
    testUser.lastSeen = LocalDate.now();
    testUser.role = role.get();
    testUser.setDepartment(department);
    testUser.phoneNumber = "0612345678";
    testUser.tags = "tag1,tag2";

    userRepository.save(testUser);

    // Test if the endpoint returns the user
    mockMvc
        .perform(MockMvcRequestBuilders.get("/user/" + testUser.id))
        .andExpect(
            result -> {
              String response = result.getResponse().getContentAsString();
              assert response.contains(testUser.name);
              assert response.contains(testUser.email);
              assert response.contains(testUser.info);
              assert response.contains(testUser.phoneNumber);
              assert response.contains(testUser.tags);
            })
        .andExpect(status().isOk());
  }

  @Test
  public void getUserThatDoesntExist() throws Exception {
    var x = mockMvc.perform(MockMvcRequestBuilders.get("/user/44"));
    x.andExpect(status().isNotFound());
  }

  @Test
  public void testUpdateUserProfile() throws Exception {

    User testUser = userRepository.findById(1L).get();

    User updateToUser = new User();
    updateToUser.name = "Bakker Jan";
    updateToUser.email = "Bakker@Jan.nl";
    updateToUser.info = "Bakker Jan bakt graag bij bakker bart.";
    updateToUser.isEmailPublic = false;
    updateToUser.isPhoneNumberPublic = false;
    updateToUser.phoneNumber = "0687654321";
    updateToUser.tags = "tag3,tag4";

    mockMvc
        .perform(
            put("/user/" + testUser.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateToUser)))
        .andExpect(status().isOk());
  }
}
