package matchmaker.backend.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.constants.ChallengeVisibility;
import matchmaker.backend.models.*;
import matchmaker.backend.repositories.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
                        .param("imgData", "true")) // Add this line if you want to include image data in the response
                .andExpect(status().isOk()) // Check if the status code is OK (200)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.author").exists())
                // Add more assertions as needed based on your Image class properties
                .andExpect(jsonPath("$.data").doesNotExist());
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
                        .param("imgData", "true")) // Add this line if you want to include image data in the response
                .andExpect(status().isOk()) // Check if the status code is OK (200)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.author").exists())
                // Add more assertions as needed based on your Image class properties
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}
