package matchmaker.backend.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import matchmaker.backend.AuthInterceptor;
import matchmaker.backend.constants.ChallengeReactionType;
import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.constants.ChallengeVisibility;
import matchmaker.backend.controllers.ChallengeInputController;
import matchmaker.backend.controllers.UserController;
import matchmaker.backend.models.*;
import matchmaker.backend.repositories.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ChallengeInputIntegrationTest {

  @Autowired private ChallengeInputRepository challengeInputRepository;
  @Autowired private ChallengeRepository challengeRepository;

  @Autowired private CompanyRepository companyRepository;

  @Autowired private DepartmentRepository departmentRepository;

  @Autowired private MockMvc mockMvc;
  @InjectMocks private AuthInterceptor authInterceptor;

  @Mock private FirebaseApp firebaseApp;

  @Mock private Environment environment;

  @Mock private UserRepository userRepository;

  private Logger log = LoggerFactory.getLogger(ChallengeInputIntegrationTest.class);
  @Autowired private UserController userController;

  @Autowired private ChallengeInputController challengeInputController;
  @Autowired private RoleRepository roleRepository;

  @Test
  public void testGetAllReactionsForChallengeById() throws Exception {
    // Create a challenge
    Challenge testChallenge = new Challenge("TestChallenge");
    testChallenge.setDescription("Description");
    testChallenge.setCreatedAt(LocalDate.now());
    testChallenge.setTitle("Titel van de challenge");

    // Create a reaction
    ChallengeInput reactionOne = new ChallengeInput("Reaction one");
    reactionOne.setChallenge(testChallenge);
    ChallengeInput reactionTwo = new ChallengeInput("Reaction two");
    reactionTwo.setChallenge(testChallenge);
    challengeRepository.save(testChallenge);

    challengeInputRepository.save(reactionOne);
    challengeInputRepository.save(reactionTwo);

    // test the reaction
    mockMvc
        .perform(MockMvcRequestBuilders.get("/reaction/challenge/" + testChallenge.getId()))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              String response = result.getResponse().getContentAsString();
              assert response.contains(reactionOne.text);
              assert response.contains(reactionTwo.text);
            });
  }

  @Test
  public void testGetChallengeReactionById() throws Exception {
    // Create a challenge
    Challenge newChallenge = new Challenge("Reaction integration test challenge");
    newChallenge.setTitle("Integration test title");
    newChallenge.setDescription("Integration test description");
    // Save the challenge
    challengeRepository.save(newChallenge);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/reaction/" + newChallenge.getId()))
        .andReturn()
        .getResponse()
        .getContentAsString()
        .equals(newChallenge);
  }

  @Test
  public void testGetNonExistentReactionById() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/reaction/9999999999"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testMarkReactionAsChosen() throws Exception {
    Challenge challenge = new Challenge("Mark reaction as chosen challenge");
    challenge.setStatus(ChallengeStatus.OPEN_VOOR_IDEEEN);
    Company testCompany = new Company("Integration company");
    Department testDepartment = new Department("Integration department", testCompany);
    companyRepository.save(testCompany);
    departmentRepository.save(testDepartment);
    challenge.setDepartment(testDepartment);
    challengeRepository.save(challenge);
    ChallengeInput newReaction = new ChallengeInput("Reaction for mark reaction test");
    newReaction.setChallenge(challenge);
    challengeInputRepository.save(newReaction);

    mockMvc
        .perform(MockMvcRequestBuilders.put("/reaction/" + newReaction.getId() + "/markreaction"))
        .andReturn()
        .getResponse()
        .getContentAsString()
        .equals(newReaction);
  }

  @Test
  public void testMarkReactionAsChosenNotFound() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.put("/reaction/991118/markreaction"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testMarkReactionAsChosenWrongStatus() throws Exception {
    Challenge challenge = new Challenge("Integration test Mark reaction wrong status");
    challenge.setStatus(ChallengeStatus.IN_UITVOERING);
    challengeRepository.save(challenge);
    ChallengeInput newReaction = new ChallengeInput("Reaction for mark reaction test");
    newReaction.setChallenge(challenge);
    challengeInputRepository.save(newReaction);

    mockMvc
        .perform(MockMvcRequestBuilders.put("/reaction/" + newReaction.getId() + "/markreaction"))
        .andExpect(status().isExpectationFailed());
  }

  @Test
  public void createReactionOnChallengeSuccesful() throws Exception {
    ChallengeInput input = new ChallengeInput("Challenge reaction");
    Challenge challenge = new Challenge("Reaction creation");
    challenge.setVisibility(ChallengeVisibility.PUBLIC);
    challenge.setStatus(ChallengeStatus.OPEN_VOOR_IDEEEN);
    input.setText("Reaction!");
    input.setType(ChallengeReactionType.FEEDBACK);
    challengeRepository.save(challenge);
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/reaction/create/" + challenge.getId())
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
