package matchmaker.backend.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import matchmaker.backend.controllers.BranchController;
import matchmaker.backend.models.Branch;
import matchmaker.backend.models.CompanyRequest;
import matchmaker.backend.models.Role;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CompanyRequestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public BranchRepository branchRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public CompanyRequestRepository companyRequestRepository;

    @Autowired
    public ChallengeRepository challengeRepository;

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
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Bakker Bart"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Test Request"));
    }

    @Test
    public void testCreateCompanyRequest() throws Exception{
        Branch branch = branchRepository.findById(1L).get();
        Optional<User> user = userRepository.findById(1L);
        User testUser = user.get();
        testUser.setDepartment(null);
        userRepository.save(testUser);

        CompanyRequest companyRequest = new CompanyRequest();
        companyRequest.setName("Test Request");
        companyRequest.setTags("ICT");
        companyRequest.setBranch(branch);
        companyRequestRepository.save(companyRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/company/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void throwUnauthorizedWithCurrentUserNull() throws Exception{
        Branch branch = branchRepository.findById(1L).get();
        Optional<User> user = userRepository.findById(1L);
        User testUser = user.get();
        challengeRepository.deleteAllByAuthorId(testUser.getId());
        userRepository.delete(testUser);

        CompanyRequest companyRequest = new CompanyRequest();
        companyRequest.setName("Test Request");
        companyRequest.setTags("ICT");
        companyRequest.setBranch(branch);
        companyRequestRepository.save(companyRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/company/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void throwBadRequestIfUserIsInCompany() throws Exception{
        Branch branch = branchRepository.findById(1L).get();

        CompanyRequest companyRequest = new CompanyRequest();
        companyRequest.setName("Test Request");
        companyRequest.setTags("ICT");
        companyRequest.setBranch(branch);
        companyRequestRepository.save(companyRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/company/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void throwBadRequestIfNameIsNullOrBlank() throws Exception{
        Branch branch = branchRepository.findById(1L).get();
        Optional<User> user = userRepository.findById(1L);
        User testUser = user.get();
        testUser.department = null;
        userRepository.save(testUser);

        CompanyRequest companyRequest = new CompanyRequest();
        companyRequest.setTags("ICT");
        companyRequest.setBranch(branch);
        companyRequestRepository.save(companyRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/company/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void throwBadRequestIfBranchIsEmpty() throws Exception{
        Optional<User> user = userRepository.findById(1L);
        User testUser = user.get();
        testUser.department = null;
        userRepository.save(testUser);

        CompanyRequest companyRequest = new CompanyRequest();
        companyRequest.setName("Test Request");
        companyRequest.setTags("ICT");
        companyRequestRepository.save(companyRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/company/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void throwNotFoundIfBranchDoesNotExist() throws Exception{
        Optional<User> user = userRepository.findById(1L);
        User testUser = user.get();
        testUser.department = null;
        userRepository.save(testUser);

        Branch branch = new Branch();
        branchRepository.save(branch);

        CompanyRequest companyRequest = new CompanyRequest();
        companyRequest.setName("Test Request");
        companyRequest.setTags("ICT");
        companyRequest.setBranch(branch);
        branchRepository.delete(branch);

        mockMvc.perform(MockMvcRequestBuilders.post("/company/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyRequest)))
                .andExpect(status().isNotFound());
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
        assert !companyRequestRepository.existsById(companyRequest.id);
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
        assert !companyRequestRepository.existsById(companyRequest.id);
    }
}
