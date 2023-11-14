package matchmaker.backend.IntegrationTests;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import lombok.SneakyThrows;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {


    @Autowired
    private MockMvc mockMvc;



    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private static final Logger log = LoggerFactory.getLogger(CompanyIntegrationTest.class);


    // FROM HERE ONWARDS, THE CODE IS NEEDED FOR THE AUTH INTERCEPTOR TO WORK
    @Mock
    private AuthInterceptor authInterceptor;

    @MockBean
    private FirebaseApp firebaseApp;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    @SneakyThrows
    @BeforeEach
    public void before() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .addInterceptors(authInterceptor)
                .build();
        when(authInterceptor.preHandle(Mockito.any(), Mockito.any(), Mockito.any())).then(invocation -> {

            log.info("[Auth Interceptor] Test environment detected, returning user 1");
            Optional<User> loggedInUser = userRepository.findById(1L);
            if (loggedInUser.isEmpty()) {
                log.info("[Auth Interceptor] No matching user found for id 1");
                invocation.getArgument(0, jakarta.servlet.http.HttpServletRequest.class).setAttribute("loggedInUser", null);
                return true;
            }
            User existingUser = loggedInUser.get();
            log.info("[Auth Interceptor] Request performed by " + existingUser.name);
            invocation.getArgument(0, jakarta.servlet.http.HttpServletRequest.class).setAttribute("loggedInUser", existingUser);

            return true;
        });

    }
    // END OF CODE NEEDED FOR THE AUTH INTERCEPTOR TO WORK


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
        //Make a new user in the repository
        User testUser = new User();
        testUser.name = "Jan Bakker";
        testUser.email = "jan.bakker@mail.com";
        testUser.acceptedTosDate = new Date();
        testUser.avatarImageId = 1L;
        testUser.createdAt = new Date();
        testUser.department = null;
        testUser.firebaseId = "3WUKhR2EcvQkwP6R5R4ZOudrJQO2";
        testUser.info = "Jan Bakker bakt graag bij bakker bart.";
        testUser.isEmailPublic = true;
        testUser.isPhoneNumberPublic = true;
        testUser.lastSeen = new Date();
        testUser.role = role.get();
        testUser.setDepartment(department);
        testUser.phoneNumber = "0612345678";
        testUser.tags = "tag1,tag2";

        userRepository.save(testUser);

        //Test if the endpoint returns the user
        mockMvc.perform(MockMvcRequestBuilders.get("/user/" + testUser.id)).andExpect(result -> {
            String response = result.getResponse().getContentAsString();
            assert response.contains(testUser.name);
            assert response.contains(testUser.email);
            assert response.contains(testUser.info);
            assert response.contains(testUser.phoneNumber);
            assert response.contains(testUser.tags);
        }).andExpect(status().isOk());
    }

    @Test
    public void getUserThatDoesntExist() throws Exception {

        var x = mockMvc.perform(MockMvcRequestBuilders.get("/user/44"));
        x.andExpect(status().isNotFound());
    }
}
