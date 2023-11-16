package matchmaker.backend.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import matchmaker.backend.controllers.BranchController;
import matchmaker.backend.models.Branch;
import matchmaker.backend.models.CompanyRequest;
import matchmaker.backend.models.Role;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.BranchRepository;
import matchmaker.backend.repositories.CompanyRequestRepository;
import matchmaker.backend.repositories.RoleRepository;
import matchmaker.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyRequestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public BranchRepository branchRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public CompanyRequestRepository companyRequestRepository;

    @Test
    public void testGetAllRequest() throws Exception{
        Branch branch = branchRepository.findById(1L).get();
        User user = new User();
        user.setName("Test User");
        userRepository.save(user);

        CompanyRequest companyRequest = new CompanyRequest();
        companyRequest.setRequestedAt(new Date());
        companyRequest.setTags("ICT");
        companyRequest.setName("Test Request");
        companyRequest.setBranch(branch);
        companyRequest.setOwner(user);
        companyRequestRepository.save(companyRequest);

        mockMvc.perform(MockMvcRequestBuilders.get("/company/request")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Bakker Jan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Bakker Bart"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("Test Request"));
    }

    @Test
    public void testGradeRequestAccept() throws Exception{
        Branch branch = branchRepository.findById(1L).get();
        User user = new User();
        user.setName("Test User");
        userRepository.save(user);

        CompanyRequest companyRequest = new CompanyRequest();
        companyRequest.setRequestedAt(new Date());
        companyRequest.setTags("ICT");
        companyRequest.setName("Test Request");
        companyRequest.setBranch(branch);
        companyRequest.setOwner(user);
        companyRequestRepository.save(companyRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/company/request/" + companyRequest.id + "/accept")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGradeChallengeReject() throws Exception{
        Branch branch = branchRepository.findById(1L).get();
        User user = new User();
        user.setName("Test User");
        userRepository.save(user);

        CompanyRequest companyRequest = new CompanyRequest();
        companyRequest.setRequestedAt(new Date());
        companyRequest.setTags("ICT");
        companyRequest.setName("Test Request");
        companyRequest.setBranch(branch);
        companyRequest.setOwner(user);
        companyRequestRepository.save(companyRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/company/request/" + companyRequest.id + "/reject")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
