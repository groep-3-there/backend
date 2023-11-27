package matchmaker.backend.IntegrationTests;

import com.google.firebase.FirebaseApp;
import matchmaker.backend.AuthInterceptor;
import matchmaker.backend.controllers.CompanyController;
import matchmaker.backend.controllers.UserController;
import matchmaker.backend.models.Branch;
import matchmaker.backend.models.Company;
import matchmaker.backend.repositories.BranchRepository;
import matchmaker.backend.repositories.CompanyRepository;
import matchmaker.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CompanyIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private CompanyRepository companyRepository;
  @InjectMocks private AuthInterceptor authInterceptor;

  @Mock private FirebaseApp firebaseApp;

  @Autowired private UserRepository userRepository;

  private static final Logger log = LoggerFactory.getLogger(ChallengeInputIntegrationTest.class);

  @Autowired private UserController userController;

  @Autowired private CompanyController companyController;
  @Autowired private BranchRepository branchRepository;

  @Test
  public void testGetCompanies() throws Exception {
    Branch testBranch = branchRepository.findById(1L).get();

    // Add four companies to the repository
    Company chipSoft = new Company("ChipSoft");
    chipSoft.setInfo("Medische Software");
    chipSoft.setCreatedAt(new Date());
    chipSoft.setBranch(testBranch);
    chipSoft.setTags("Medisch, Software, Zorg");

    Company bol = new Company("Bol.com");
    bol.setInfo("Webshop");
    bol.setCreatedAt(new Date());
    bol.setBranch(testBranch);

    bol.setTags("Webshop, E-commerce, Retail");

    Company nhlStenden = new Company("NHL Stenden");
    nhlStenden.setInfo("Hogeschool");
    nhlStenden.setCreatedAt(new Date());
    nhlStenden.setBranch(testBranch);
    nhlStenden.setTags("Hogeschool, Onderwijs, HBO");

    Company google = new Company("Google");
    google.setInfo("Zoekmachine");
    google.setCreatedAt(new Date());
    google.setBranch(testBranch);
    google.setTags("Zoekmachine, Software, Internet");

    companyRepository.save(chipSoft);
    companyRepository.save(bol);
    companyRepository.save(nhlStenden);
    companyRepository.save(google);

    // Check if the companies are added to the repository
    long count = companyRepository.count();
    assert count > 0;
  }

  @Test
  public void testGetCompanyById() throws Exception {
    Branch testBranch = branchRepository.findById(1L).get();
    // Create a company and add it to the repository
    Company matchmaker = new Company("MatchMaker");
    matchmaker.setInfo("Matching Software");
    matchmaker.setCreatedAt(new Date());
    matchmaker.setBranch(testBranch);
    matchmaker.setTags("Matching, Software, There");

    companyRepository.save(matchmaker);
    mockMvc
        .perform(MockMvcRequestBuilders.get("/company/" + matchmaker.getId().toString()))
        .andExpect(
            result -> {
              String response = result.getResponse().getContentAsString();
              assert response.contains(matchmaker.getName());
              assert response.contains(matchmaker.getInfo());
              assert response.contains(matchmaker.getBranch().name);

              assert response.contains(matchmaker.getTags());
            })
        .andExpect(status().isOk());
  }

  @Test
  public void testGetNonExistentCompanyById() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/company/999912")).andExpect(status().isNotFound());
  }
}
