package matchmaker.backend.IntegrationTests;

import matchmaker.backend.models.Company;
import matchmaker.backend.models.Department;
import matchmaker.backend.models.Role;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.CompanyRepository;
import matchmaker.backend.repositories.DepartmentRepository;
import matchmaker.backend.repositories.RoleRepository;
import matchmaker.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void getUserById() throws Exception {
        Optional<Role> role = Optional.of(roleRepository.findById(1L).orElseGet(() -> new Role("testRole")));
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
        testUser.favorites = null;
        testUser.department = null;
        testUser.info = "Jan Bakker bakt graag bij bakker bart.";
        testUser.isEmailPublic = true;
        testUser.isPhoneNumberPublic = true;
        testUser.lastSeen = new Date();
        testUser.role = role.get();
        testUser.setDepartment(department);
        testUser.phoneNumber = "0612345678";
        testUser.tags = "tag1,tag2";

        userRepository.save(testUser);

        //Test if the endpoint returns the user
        mockMvc.perform(MockMvcRequestBuilders.get("/user/" + testUser.id))
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains(testUser.name);
                    assert response.contains(testUser.email);
                    assert response.contains(testUser.info);
                    assert response.contains(testUser.phoneNumber);
                    assert response.contains(testUser.tags);
                })
                .andExpect(status().isOk());
    }

    @Test
    public void getUserThatDoesntExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/44"))
                .andExpect(status().isNotFound());
    }
}
