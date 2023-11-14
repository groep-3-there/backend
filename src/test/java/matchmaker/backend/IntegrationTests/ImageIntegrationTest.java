package matchmaker.backend.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import lombok.SneakyThrows;
import matchmaker.backend.AuthInterceptor;
import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.constants.ChallengeVisibility;
import matchmaker.backend.controllers.ChallengeController;
import matchmaker.backend.controllers.ImageController;
import matchmaker.backend.models.*;
import matchmaker.backend.repositories.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ImageIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    private ImageController imageController;

    @Mock
    private AuthInterceptor authInterceptor;

    @MockBean
    private FirebaseApp firebaseApp;

    private static final Logger log = LoggerFactory.getLogger(ChallengeInputIntegrationTest.class);
    @Autowired
    private RoleRepository roleRepository;

    Long userid = 1L;
    @SneakyThrows
    @BeforeEach
    public void before() {
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
        userid = testUser.getId();
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(imageController)
                .addInterceptors(authInterceptor)
                .build();
        when(authInterceptor.preHandle(Mockito.any(), Mockito.any(), Mockito.any())).then(invocation -> {

            log.info("[Auth Interceptor] Test environment detected, returning user 1");
            Optional<User> loggedInUser = userRepository.findById(userid);
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

    private MockMultipartFile createMockMultipartFile(String name, String originalFileName) throws IOException, IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(originalFileName);
        byte[] content = StreamUtils.copyToByteArray(inputStream);
        return new MockMultipartFile(name, originalFileName, "image/jpeg", content);
    }

    public static MockMultipartFile createMockMultipartFileWithImage(String name, String originalFileName, String contentType) throws IOException {
        Path path = Paths.get("src/main/resources/", originalFileName);
        byte[] content = Files.readAllBytes(path);

        return new MockMultipartFile(name, originalFileName, contentType, content);
    }

    @Test
    public void testUploadEmptyImage() throws Exception {
        Branch testBranch = branchRepository.findById(1L).get();

        Company testCompany = new Company();
        testCompany.setName("ODIDO");
        testCompany.setBranch(testBranch);
        testCompany.setTags("Website");
        testCompany.ownerId = 1L;
        testCompany.setInfo("Dit is het bedrijf ODIDO.");
        companyRepository.save(testCompany);

        Department testDepartment = new Department();
        testDepartment.setName("ICT");
        testDepartment.setParentCompany(testCompany);
        departmentRepository.save(testDepartment);

        Challenge testChallenge = new Challenge();
        testChallenge.setTitle("nieuwe website");
        testChallenge.setDescription("Ik wil graag een nieuwe website. Ik zoek een team van 3 personen die mij hierbij kunnen helpen.");
        testChallenge.setCreatedAt(new java.util.Date());
        testChallenge.setEndDate(new java.util.Date());
        testChallenge.setSummary("nieuwe website");
        testChallenge.setDepartment(testDepartment);
        testChallenge.setStatus(ChallengeStatus.OPEN_VOOR_IDEEEN);
        testChallenge.setVisibility(ChallengeVisibility.PUBLIC);
        testChallenge.setTags("Website");
        testChallenge.setContactInformation("Berijkbaar via mail");
        challengeRepository.save(testChallenge);

        User testUser = new User();
        testUser.setName("Kees Van der Linden");
        testUser.setEmail("Kees@Hotmail.com");
        testUser.setDepartment(testDepartment);
        userRepository.save(testUser);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/image/upload")
                        .file(createMockMultipartFile("image", "test.jpg"))
                        .param("imgData", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.author").exists())
                .andExpect(jsonPath("$.photoData").isEmpty());
    }

    @Test
    void testUploadImage() throws Exception {
        Branch testBranch = branchRepository.findById(1L).get();

        Company testCompany = new Company();
        testCompany.setName("Vodafone");
        testCompany.setBranch(testBranch);
        testCompany.setTags("Website");
        testCompany.ownerId = 1L;
        testCompany.setInfo("Dit is het bedrijf Vodafone.");
        companyRepository.save(testCompany);

        Department testDepartment = new Department();
        testDepartment.setName("ICT");
        testDepartment.setParentCompany(testCompany);
        departmentRepository.save(testDepartment);

        Challenge testChallenge = new Challenge();
        testChallenge.setTitle("workflow verbeteren");
        testChallenge.setDescription("Ik wil graag mijn workflow verbeteren. Ik zoek een team van 3 personen die mij hierbij kunnen helpen.");
        testChallenge.setCreatedAt(new java.util.Date());
        testChallenge.setEndDate(new java.util.Date());
        testChallenge.setSummary("workflow verbeteren");
        testChallenge.setDepartment(testDepartment);
        testChallenge.setStatus(ChallengeStatus.OPEN_VOOR_IDEEEN);
        testChallenge.setVisibility(ChallengeVisibility.PUBLIC);
        testChallenge.setTags("Website");
        testChallenge.setContactInformation("Berijkbaar via mail");
        challengeRepository.save(testChallenge);

        User testUser = new User();
        testUser.setName("Jonathan Van der Linden");
        testUser.setEmail("Jonathan@Hotmail.com");
        testUser.setDepartment(testDepartment);
        userRepository.save(testUser);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/image/upload")
                        .file(createMockMultipartFileWithImage("image", "image.jpg", "image/jpeg"))
                        .param("imgData", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.author").exists())
                .andExpect(jsonPath("$.photoData").isNotEmpty());
    }
}
