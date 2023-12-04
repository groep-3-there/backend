package matchmaker.backend.IntegrationTests;

import com.google.firebase.FirebaseApp;
import matchmaker.backend.AuthInterceptor;
import matchmaker.backend.controllers.BranchController;
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
public class CountryIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired public CountryRepository branchRepository;

  @InjectMocks private AuthInterceptor authInterceptor;

  @Mock private FirebaseApp firebaseApp;

  @Mock private Environment environment;

  @Autowired private UserRepository userRepository;

  @Autowired private RoleRepository roleRepository;

  @Autowired private CompanyRepository companyRepository;

  @Autowired private DepartmentRepository departmentRepository;

  Long userId = 1L;
  private Logger log = LoggerFactory.getLogger(CountryIntegrationTest.class);

  @Autowired private BranchController branchController;

  @Test
  public void testGetBranches() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/country/all").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].code").value("AF"));
  }
}
