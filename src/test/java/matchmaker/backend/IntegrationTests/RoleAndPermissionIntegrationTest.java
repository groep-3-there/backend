package matchmaker.backend.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import matchmaker.backend.AuthInterceptor;
import matchmaker.backend.constants.DefaultRoleId;
import matchmaker.backend.controllers.BranchController;
import matchmaker.backend.models.Branch;
import matchmaker.backend.models.Role;
import matchmaker.backend.models.User;
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
public class RoleAndPermissionIntegrationTest {

  @Autowired private MockMvc mockMvc;


  @InjectMocks private AuthInterceptor authInterceptor;

  @Mock private FirebaseApp firebaseApp;

  @Mock private Environment environment;
  @Autowired public ObjectMapper objectMapper;

  @Autowired private PermissionRepository permissionRepository;
  @Autowired private RoleRepository roleRepository;
  @Autowired private UserRepository userRepository;

  private Logger log = LoggerFactory.getLogger(RoleAndPermissionIntegrationTest.class);


  public void setUserOneMatchmaker() {
    userRepository.findById(1L).get().setRole(roleRepository.findById(DefaultRoleId.MATCHMAKER).get());
  }
  public void setUserOneMedewerker() {
    userRepository.findById(1L).get().setRole(roleRepository.findById(DefaultRoleId.MEDEWERKER).get());
  }

  @Test
  @Transactional
  public void getRoleAsMatchmaker() throws Exception {
    setUserOneMatchmaker();
    mockMvc
        .perform(MockMvcRequestBuilders.get("/role/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("name").value("Medewerker"));
  }
  @Test
  @Transactional
  public void getAllRolesAsMatchmaker() throws Exception {
    setUserOneMatchmaker();
    mockMvc
            .perform(MockMvcRequestBuilders.get("/role").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Medewerker"));
  }
  @Test
  @Transactional
  public void createRoleAsMatchmaker() throws Exception {
    setUserOneMatchmaker();
    mockMvc
            .perform(MockMvcRequestBuilders.post("/role").content(objectMapper.writeValueAsString(new Role("Medewerker"))).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("name").value("Medewerker"));
  }
  @Test
  @Transactional
  public void deleteRoleAsMatchmaker() throws Exception {
    setUserOneMatchmaker();
    mockMvc
            .perform(MockMvcRequestBuilders.delete("/role/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    mockMvc.perform(MockMvcRequestBuilders.get("/role/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

  }
  @Test
  @Transactional
  public void updateRoleAsMatchmaker() throws Exception {
    setUserOneMatchmaker();
    Role r = roleRepository.findById(1L).get();
    r.isAssignable = false;
    mockMvc
            .perform(MockMvcRequestBuilders.put("/role/1").content(objectMapper.writeValueAsString(r)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("isAssignable").value(false));
  }


  @Test
  @Transactional
  public void getRoleAsRegularUser() throws Exception {
    setUserOneMatchmaker();
    mockMvc
            .perform(MockMvcRequestBuilders.get("/role/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("name").value("Medewerker"));
  }
  @Test
  @Transactional
  public void getAllRolesAsRegularUser() throws Exception {
    setUserOneMedewerker();
    mockMvc
            .perform(MockMvcRequestBuilders.get("/role").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Medewerker"));
  }
  @Test
  @Transactional
  public void createRoleAsRegularUserShouldThrowUnauthorized() throws Exception {
    setUserOneMedewerker();
    mockMvc
            .perform(MockMvcRequestBuilders.post("/role").content(objectMapper.writeValueAsString(new Role("Medewerker"))).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
  }
  @Test
  @Transactional
  public void deleteRoleAsRegularUserShouldThrowUnauthorized() throws Exception {
    setUserOneMedewerker();
    mockMvc
            .perform(MockMvcRequestBuilders.delete("/role/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    mockMvc.perform(MockMvcRequestBuilders.get("/role/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

  }
  @Test
  @Transactional
  public void updateRoleAsRegularUserShouldThrowUnauthorized() throws Exception {
    setUserOneMedewerker();
    Role r = roleRepository.findById(1L).get();
    r.isAssignable = false;
    mockMvc
            .perform(MockMvcRequestBuilders.put("/role/1").content(objectMapper.writeValueAsString(r)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
  }

}
