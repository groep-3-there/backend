package matchmaker.backend.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import lombok.SneakyThrows;
import matchmaker.backend.AuthInterceptor;
import matchmaker.backend.constants.ChallengeReactionType;
import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.constants.ChallengeVisibility;
import matchmaker.backend.controllers.ChallengeInputController;
import matchmaker.backend.controllers.CompanyController;
import matchmaker.backend.controllers.UserController;
import matchmaker.backend.models.*;
import matchmaker.backend.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ChallengeInputIntegrationTest {

    @Autowired
    private ChallengeInputRepository challengeInputRepository;
    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private AuthInterceptor authInterceptor;

    @MockBean
    private FirebaseApp firebaseApp;

    @Autowired
    private UserRepository userRepository;


    private Logger log = LoggerFactory.getLogger(ChallengeInputIntegrationTest.class);
    @Autowired
    private UserController userController;

    @Autowired
    private ChallengeInputController challengeInputController;
    @Autowired
    private RoleRepository roleRepository;

    @SneakyThrows
    @BeforeEach
    public void before() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(challengeInputController)
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

    }
    @Test
    public void testGetAllReactionsForChallengeById() throws Exception{
        //Create a challenge
        Challenge testChallenge = new Challenge("TestChallenge");
        testChallenge.setDescription("Description");
        testChallenge.setCreatedAt(new Date());
        testChallenge.setTitle("Titel van de challenge");

        //Create a reaction
        ChallengeInput reactionOne = new ChallengeInput("Reaction one");
        reactionOne.setChallenge(testChallenge);
        ChallengeInput reactionTwo = new ChallengeInput("Reaction two");
        reactionTwo.setChallenge(testChallenge);
        challengeRepository.save(testChallenge);

        challengeInputRepository.save(reactionOne);
        challengeInputRepository.save(reactionTwo);

        //test the reaction
        mockMvc.perform(MockMvcRequestBuilders.get("/reaction/challenge/" + testChallenge.getId()))
                .andExpect(status().isOk())
                .andExpect(result ->{
                    String response = result.getResponse().getContentAsString();
                    assert response.contains(reactionOne.text);
                    assert response.contains(reactionTwo.text);
                        });
    }

    @Test
    public void testGetChallengeReactionById() throws Exception{
        //Create a challenge
        Challenge newChallenge = new Challenge("Reaction integration test challenge");
        newChallenge.setTitle("Integration test title");
        newChallenge.setDescription("Integration test description");
        //Save the challenge
        challengeRepository.save(newChallenge);

        mockMvc.perform(MockMvcRequestBuilders.get("/reaction/" + newChallenge.getId()))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .equals(newChallenge);
    }

    @Test
    public void testGetNonExistentReactionById() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/reaction/9999999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testMarkReactionAsChosen() throws Exception{
        Challenge challenge = new Challenge("Mark reaction as chosen challenge");
        challenge.setStatus(ChallengeStatus.OPEN_VOOR_IDEEEN);
        Company testCompany = new Company("Integration company");
        Department testDepartment = new Department("Integration department", testCompany);
        companyRepository.save(testCompany);
        departmentRepository.save(testDepartment);
        challenge.setCompany(testCompany);
        challenge.setDepartment(testDepartment);
        challengeRepository.save(challenge);
        ChallengeInput newReaction = new ChallengeInput("Reaction for mark reaction test");
        newReaction.setChallenge(challenge);
        challengeInputRepository.save(newReaction);

        mockMvc.perform(MockMvcRequestBuilders.put("/reaction/" + newReaction.getId() + "/markreaction"))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .equals(newReaction);
    }

    @Test
    public void testMarkReactionAsChosenNotFound() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/reaction/991118/markreaction"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testMarkReactionAsChosenWrongStatus() throws Exception{
        Challenge challenge = new Challenge("Integration test Mark reaction wrong status");
        challenge.setStatus(ChallengeStatus.IN_UITVOERING);
        challengeRepository.save(challenge);
        ChallengeInput newReaction = new ChallengeInput("Reaction for mark reaction test");
        newReaction.setChallenge(challenge);
        challengeInputRepository.save(newReaction);

        mockMvc.perform(MockMvcRequestBuilders.put("/reaction/" + newReaction.getId() + "/markreaction"))
                .andExpect(status().isExpectationFailed());
    }

    @Test
    public void createReactionOnChallengeSuccesful() throws Exception{
        ChallengeInput input = new ChallengeInput("Challenge reaction");
        Challenge challenge = new Challenge("Reaction creation");
        challenge.setVisibility(ChallengeVisibility.PUBLIC);
        challenge.setStatus(ChallengeStatus.OPEN_VOOR_IDEEEN);
        input.setText("Reaction!");
        input.setType(ChallengeReactionType.FEEDBACK);
        challengeRepository.save(challenge);




        mockMvc.perform(MockMvcRequestBuilders.post("/reaction/create/" + challenge.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(input)))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

