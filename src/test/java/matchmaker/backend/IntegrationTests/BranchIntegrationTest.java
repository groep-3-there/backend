package matchmaker.backend.IntegrationTests;

import com.google.firebase.FirebaseApp;
import matchmaker.backend.AuthInterceptor;
import matchmaker.backend.controllers.BranchController;
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
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BranchIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired public BranchRepository branchRepository;

  @InjectMocks private AuthInterceptor authInterceptor;

  @Mock private FirebaseApp firebaseApp;

  @Mock private Environment environment;

  @Autowired private UserRepository userRepository;

  @Autowired private RoleRepository roleRepository;

  @Autowired private CompanyRepository companyRepository;

  @Autowired private DepartmentRepository departmentRepository;

  Long userId = 1L;
  private Logger log = LoggerFactory.getLogger(BranchIntegrationTest.class);

  @Autowired private BranchController branchController;

  @Test
  public void testGetBranches() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/branch/all").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Advies en consultancy"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Agrosector"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$[2].name")
                .value("Bouw, installatie en infrastructuur"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[3].name").value("Cultuur en sport"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[4].name").value("Delfstoffen"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[5].name").value("Financiële dienstverlening"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$[6].name")
                .value("Gezondheidszorg en maatschappelijke dienstverlening"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$[7].name")
                .value("Autohandel, groothandel en detailhandel"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[8].name").value("Horeca"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[9].name").value("ICT, media en communicatie"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[10].name").value("Industrie"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[11].name").value("Onderwijs en training"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[12].name").value("Onroerend goed"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$[13].name")
                .value("Persoonlijke dienstverleningen en not-for-profit"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[14].name").value("Vervoer, post en opslag"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[15].name").value("Water en afval"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[16].name").value("Zakelijke dienstverlening"));
  }

  @Test
  public void testAddingNewBranche() throws Exception {
    Branch newbranche = new Branch();
    newbranche.setName("New branch");
    branchRepository.save(newbranche);
    assert branchRepository.existsById(newbranche.getId());
  }
}
