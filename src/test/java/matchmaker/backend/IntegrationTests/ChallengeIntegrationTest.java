package matchmaker.backend.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import matchmaker.backend.AuthInterceptor;
import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.constants.ChallengeVisibility;
import matchmaker.backend.controllers.ChallengeController;
import matchmaker.backend.controllers.CompanyController;
import matchmaker.backend.controllers.ImageController;
import matchmaker.backend.controllers.UserController;
import matchmaker.backend.models.*;
import matchmaker.backend.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleStatus;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ChallengeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public ChallengeRepository challengeRepository;
    @Autowired
    public CompanyRepository companyRepository;
    @Autowired
    public DepartmentRepository departmentRepository;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public RoleRepository rolerepository;
    @Autowired
    public BranchRepository branchRepository;

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    private UserController userController;

    @Autowired
    private ChallengeController challengeController;
    @InjectMocks
    private AuthInterceptor authInterceptor;

    @Mock
    private FirebaseApp firebaseApp;

    private static final Logger log = LoggerFactory.getLogger(ChallengeInputIntegrationTest.class);

    @Test
    public void testChallengesTableNotEmpty() throws Exception {
        Branch testBranch = branchRepository.findById(1L).get();

        Company testCompany = new Company();
        testCompany.setName("Paypal");
        testCompany.setBranch(testBranch);
        testCompany.setTags("Website");
        testCompany.ownerId = 1L;
        testCompany.setInfo("Dit is het bedrijf ChipSoft");
        companyRepository.save(testCompany);

        Department testDepartment = new Department();
        testDepartment.setName("ICT");
        testDepartment.setParentCompany(testCompany);
        departmentRepository.save(testDepartment);

        Role role = rolerepository.findById(2L).get();

        User testUser = new User();
        testUser.setName("Eline Van der Linden");
        testUser.setEmail("Eline@Hotmail.com");
        testUser.setDepartment(testDepartment);
        testUser.setRole(role);
        userRepository.save(testUser);

        Challenge testChallenge = new Challenge();
        testChallenge.setTitle("Innovatie kapperszaak");
        testChallenge.setDescription("Ik wil graag mijn kapperszaak innoveren. Ik zoek een team van 3 personen die mij hierbij kunnen helpen.");
        testChallenge.setCreatedAt(new java.util.Date());
        testChallenge.setEndDate(new java.util.Date());
        testChallenge.setSummary("Kapperszaak Innoveren");
        testChallenge.setDepartment(testDepartment);
        testChallenge.setStatus(ChallengeStatus.OPEN_VOOR_IDEEEN);
        testChallenge.setVisibility(ChallengeVisibility.PUBLIC);
        testChallenge.setAuthor(testUser);
        testChallenge.setTags("Website");
        challengeRepository.save(testChallenge);

        mockMvc.perform(MockMvcRequestBuilders.get("/challenge")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetChallengeById() throws Exception {
        Branch testBranch = branchRepository.findById(1L).get();

        Company testCompany = new Company();
        testCompany.setName("Philips");
        testCompany.setBranch(testBranch);
        testCompany.setTags("Website");
        testCompany.ownerId = 1L;
        testCompany.setInfo("Dit is het bedrijf Philips");
        companyRepository.save(testCompany);

        Department testDepartment = new Department();
        testDepartment.setName("ICT");
        testDepartment.setParentCompany(testCompany);
        departmentRepository.save(testDepartment);

        Role role = rolerepository.findById(1L).get();

        User testUser = new User();
        testUser.setName("Phillip van der Linden");
        testUser.setEmail("Phillip@Hotmail.com");
        testUser.setDepartment(testDepartment);
        testUser.setRole(role);
        userRepository.save(testUser);

        Challenge testChallenge = new Challenge();
        testChallenge.setTitle("Zuivere Koffie");
        testChallenge.setDescription("Ik wil graag mijn bedrijf innoveren. Ik zoek een team van 3 personen die mij hierbij kunnen helpen.");
        testChallenge.setCreatedAt(new java.util.Date());
        testChallenge.setEndDate(new java.util.Date());
        testChallenge.setSummary("Bedrijf Innoveren");
        testChallenge.setDepartment(testDepartment);
        testChallenge.setStatus(ChallengeStatus.OPEN_VOOR_IDEEEN);
        testChallenge.setVisibility(ChallengeVisibility.PUBLIC);
        testChallenge.setAuthor(testUser);
        testChallenge.setTags("Website");
        challengeRepository.save(testChallenge);

        mockMvc.perform(MockMvcRequestBuilders.get("/challenge/" + testChallenge.id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Zuivere Koffie"));
    }

    @Test
    public void testCreateChallenge() throws Exception {
        Branch testBranch = branchRepository.findById(1L).get();

        Company testCompany = new Company();
        testCompany.setName("ASML");
        testCompany.setBranch(testBranch);
        testCompany.setTags("Website");
        testCompany.ownerId = 1L;
        testCompany.setInfo("Dit is het bedrijf ASML");
        companyRepository.save(testCompany);

        Department testDepartment = new Department();
        testDepartment.setName("ICT");
        testDepartment.setParentCompany(testCompany);
        departmentRepository.save(testDepartment);

        Role role = rolerepository.findById(2L).get();

        User user = new User();
        user.setName("Lars Van der Linden");
        user.setEmail("Lars@Hotmail.com");
        user.setDepartment(testDepartment);
        user.setRole(role);
        userRepository.save(user);

        //change the role of the first user, delete later when login is implemented
        User firstuser = userRepository.findById(1L).get();
        firstuser.setRole(role);
        userRepository.save(firstuser);

        Challenge testChallenge = new Challenge();
        testChallenge.setTitle("Vergadering verbeteren");
        testChallenge.setDescription("Ik wil graag mijn vergadering verbeteren. Ik zoek een team van 3 personen die mij hierbij kunnen helpen.");
        testChallenge.setCreatedAt(new java.util.Date());
        testChallenge.setEndDate(new java.util.Date());
        testChallenge.setSummary("Vergadering verbeteren");
        testChallenge.setDepartment(testDepartment);
        testChallenge.setStatus(ChallengeStatus.OPEN_VOOR_IDEEEN);
        testChallenge.setVisibility(ChallengeVisibility.PUBLIC);
        testChallenge.setTags("Website");
        testChallenge.setContactInformation("Berijkbaar via mail");

        mockMvc.perform(post("/challenge")
            .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testChallenge)))
                .andExpect(status().isOk());

        //reset the change of the first user, delete later when login is implemented
        Role roletemp = rolerepository.findById(1L).get();
        firstuser.setRole(roletemp);
        userRepository.save(firstuser);
    }

    @Test
    public void testUpdateChallenge() throws Exception {
        Branch testBranch = branchRepository.findById(1L).get();

        Company testCompany = new Company();
        testCompany.setName("CJIB");
        testCompany.setBranch(testBranch);
        testCompany.setTags("Website");
        testCompany.ownerId = 1L;
        testCompany.setInfo("Dit is het bedrijf CJIB");
        companyRepository.save(testCompany);

        Department testDepartment;

        User firstuser = userRepository.findById(1L).get();
        if (firstuser.id != null) {
            testDepartment = firstuser.getDepartment();
        }
        else {
            testDepartment = new Department();
            testDepartment.setName("ICT");
            testDepartment.setParentCompany(testCompany);
            departmentRepository.save(testDepartment);
        }

        Role role = rolerepository.findById(2L).get();

        User user = new User();
        user.setName("Piet Van der Linden");
        user.setEmail("Piet@Hotmail.com");
        user.setDepartment(testDepartment);
        user.setRole(role);
        userRepository.save(user);

        Challenge testChallenge = new Challenge();
        testChallenge.setTitle("Aanpassing website");
        testChallenge.setDescription("Ik wil graag mijn website verbeteren. Ik zoek een team van 3 personen die mij hierbij kunnen helpen.");
        testChallenge.setCreatedAt(new java.util.Date());
        testChallenge.setEndDate(new java.util.Date());
        testChallenge.setSummary("website verbeteren");
        testChallenge.setDepartment(testDepartment);
        testChallenge.setStatus(ChallengeStatus.OPEN_VOOR_IDEEEN);
        testChallenge.setVisibility(ChallengeVisibility.PUBLIC);
        testChallenge.setTags("Website");
        testChallenge.setContactInformation("Berijkbaar via mail");
        testChallenge.setAuthor(user);
        challengeRepository.save(testChallenge);


        challengeRepository.findById(testChallenge.id).get();
        testChallenge.setTitle("Aanpassing platform");
        testChallenge.setDescription("Ik wil graag mijn platform verbeteren. Ik zoek een team van 3 personen die mij hierbij kunnen helpen.");
        testChallenge.setSummary("platform verbeteren");
        testChallenge.setTags("App");

        //change the role of the first user, delete later when login is implemented
        User firstUser = userRepository.findById(1L).get();
        firstUser.setRole(role);
        userRepository.save(firstUser);

        mockMvc.perform(put("/challenge/update")
            .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testChallenge)))
                .andExpect(status().isOk());

        //reset the change of the first user, delete later when login is implemented
        Role roletemp = rolerepository.findById(1L).get();
        firstUser.setRole(roletemp);
        userRepository.save(firstUser);
    }

    @Test
    public void testSearchChallenges() throws Exception {
        Branch testBranch = branchRepository.findById(1L).get();

        Company testCompany = new Company();
        testCompany.setName("Jumbo");
        testCompany.setBranch(testBranch);
        testCompany.setTags("Website");
        testCompany.ownerId = 1L;
        testCompany.setInfo("Dit is het bedrijf Jumbo");
        companyRepository.save(testCompany);

        Department testDepartment = new Department();
        testDepartment.setName("ICT");
        testDepartment.setParentCompany(testCompany);
        departmentRepository.save(testDepartment);

        Role role = rolerepository.findById(1L).get();

        User user = new User();
        user.setName("Linda Van der Linden");
        user.setEmail("Linda@Hotmail.com");
        user.setDepartment(testDepartment);
        user.setRole(role);
        userRepository.save(user);

        Challenge testChallenge = new Challenge();
        testChallenge.setTitle("Management verbeteren");
        testChallenge.setDescription("Ik wil graag mijn management verbeteren. Ik zoek een team van 3 personen die mij hierbij kunnen helpen.");
        testChallenge.setCreatedAt(new java.util.Date());
        testChallenge.setEndDate(new java.util.Date());
        testChallenge.setSummary("Management verbeteren");
        testChallenge.setDepartment(testDepartment);
        testChallenge.setStatus(ChallengeStatus.OPEN_VOOR_IDEEEN);
        testChallenge.setVisibility(ChallengeVisibility.PUBLIC);
        testChallenge.setTags("Website");
        challengeRepository.save(testChallenge);

        //alleen op query
        mockMvc.perform(MockMvcRequestBuilders.get("/challenge/search?query=Management")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(testChallenge.id));

        //query en company
        mockMvc.perform(MockMvcRequestBuilders.get("/challenge/search?query=Management&company=Jumbo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(testChallenge.id));

        //query, company en branch
        mockMvc.perform(MockMvcRequestBuilders.get("/challenge/search?query=Management&company=Jumbo&branche=Advies en consultancy")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(testChallenge.id));

        //query en branch
        mockMvc.perform(MockMvcRequestBuilders.get("/challenge/search?query=Management&branche=Advies en consultancy")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(testChallenge.id));

        //alleen branch
        mockMvc.perform(MockMvcRequestBuilders.get("/challenge/search?branche=Advies en consultancy")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(testChallenge.id));

        //company en branch
        mockMvc.perform(MockMvcRequestBuilders.get("/challenge/search?company=Jumbo&branche=Advies en consultancy")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(testChallenge.id));

        //alleen company
        mockMvc.perform(MockMvcRequestBuilders.get("/challenge/search?company=Jumbo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(testChallenge.id));
    }
}
