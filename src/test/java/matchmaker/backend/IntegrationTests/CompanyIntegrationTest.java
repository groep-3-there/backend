package matchmaker.backend.IntegrationTests;

import com.google.firebase.FirebaseApp;
import lombok.SneakyThrows;
import matchmaker.backend.AuthInterceptor;
import matchmaker.backend.controllers.CompanyController;
import matchmaker.backend.controllers.UserController;
import matchmaker.backend.models.Company;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.CompanyRepository;
import matchmaker.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyIntegrationTest{

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private CompanyRepository companyRepository;
    @Mock
    private AuthInterceptor authInterceptor;

    @MockBean
    private FirebaseApp firebaseApp;

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(ChallengeInputIntegrationTest.class);

    @Autowired
    private UserController userController;

    @Autowired
    private CompanyController companyController;

    @SneakyThrows
    @BeforeEach
    public void before() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(companyController)
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
    public void testGetCompanies() throws Exception{
        //Add four companies to the repository
        Company chipSoft = new Company("ChipSoft");
        chipSoft.setInfo("Medische Software");
        chipSoft.setCreatedAt(new Date());
        chipSoft.setBranch("Software");
        chipSoft.setTags("Medisch, Software, Zorg");

        Company bol = new Company("Bol.com");
        bol.setInfo("Webshop");
        bol.setCreatedAt(new Date());
        bol.setBranch("Webshop");
        bol.setTags("Webshop, E-commerce, Retail");

        Company nhlStenden = new Company("NHL Stenden");
        nhlStenden.setInfo("Hogeschool");
        nhlStenden.setCreatedAt(new Date());
        nhlStenden.setBranch("Onderwijs");
        nhlStenden.setTags("Hogeschool, Onderwijs, HBO");

        Company google = new Company("Google");
        google.setInfo("Zoekmachine");
        google.setCreatedAt(new Date());
        google.setBranch("Zoekmachine");
        google.setTags("Zoekmachine, Software, Internet");

        companyRepository.save(chipSoft);
        companyRepository.save(bol);
        companyRepository.save(nhlStenden);
        companyRepository.save(google);

        //Check if the companies are added to the repository
        long count = companyRepository.count();
        assert count > 0;
    }

    @Test
    public void testGetCompanyById() throws Exception{
        //Create a company and add it to the repository
        Company matchmaker = new Company("MatchMaker");
        matchmaker.setInfo("Matching Software");
        matchmaker.setCreatedAt(new Date());
        matchmaker.setBranch("Software");
        matchmaker.setTags("Matching, Software, There");

        companyRepository.save(matchmaker);
        mockMvc.perform(MockMvcRequestBuilders.get("/company/" + matchmaker.getId().toString()))
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains(matchmaker.getName());
                    assert response.contains(matchmaker.getInfo());
                    assert response.contains(matchmaker.getBranch());
                    assert response.contains(matchmaker.getTags());
                })
                .andExpect(status().isOk());
    }

    @Test
    public void testGetNonExistentCompanyById() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/company/999912"))
                .andExpect(status().isNotFound());
    }
}
