package matchmaker.backend.IntegrationTests;

import com.google.firebase.FirebaseApp;
import lombok.SneakyThrows;
import matchmaker.backend.AuthInterceptor;
import matchmaker.backend.controllers.BranchController;
import matchmaker.backend.models.Company;
import matchmaker.backend.models.Department;
import matchmaker.backend.models.Role;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.*;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BranchIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public BranchRepository branchRepository;

    @Mock
    private AuthInterceptor authInterceptor;

    @MockBean
    private FirebaseApp firebaseApp;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    Long userId = 1L;
    private Logger log = LoggerFactory.getLogger(BranchIntegrationTest.class);

    @Autowired
    private BranchController branchController;
    @SneakyThrows
    @BeforeEach
    public void before() {
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
        userId = testUser.id;
        log.info("User saved to repository with id " + testUser.id.toString());
        userRepository.findAll().forEach(user -> log.info("Database users: " + user.id.toString()));

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(branchController)
                .addInterceptors(authInterceptor)
                .build();
        when(authInterceptor.preHandle(Mockito.any(), Mockito.any(), Mockito.any())).then(invocation -> {

            log.info("[Auth Interceptor] Test environment detected, returning user 1");
            Optional<User> loggedInUser = userRepository.findById(userId);
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

    @Test
    public void testGetBranches() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/branch/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Advies en consultancy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Agrosector"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("Bouw, installatie en infrastructuur"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].name").value("Cultuur en sport"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].name").value("Delfstoffen"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[5].name").value("Financiële dienstverlening"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[6].name").value("Gezondheidszorg en maatschappelijke dienstverlening"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[7].name").value("Autohandel, groothandel en detailhandel"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[8].name").value("Horeca"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[9].name").value("ICT, media en communicatie"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[10].name").value("Industrie"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[11].name").value("Onderwijs en training"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[12].name").value("Onroerend goed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[13].name").value("Persoonlijke dienstverleningen en not-for-profit"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[14].name").value("Vervoer, post en opslag"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[15].name").value("Water en afval"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[16].name").value("Zakelijke dienstverlening"));
    }
}
