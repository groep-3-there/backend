package matchmaker.backend.IntegrationTests;

import matchmaker.backend.models.Department;
import matchmaker.backend.repositories.DepartmentCodeRepository;
import matchmaker.backend.repositories.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DepartmentCodeIntegrationTest {
  @Autowired private MockMvc mockMvc;

  @Autowired public DepartmentCodeRepository departmentCodeRepository;

  @Autowired public DepartmentRepository departmentRepository;

  @Test
  public void getDepartmentCode() throws Exception {
    Department testDepartment = new Department();
    testDepartment.setName("Test Department");
    departmentRepository.save(testDepartment);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/departmentcode/" + testDepartment.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").exists())
        .andExpect(jsonPath("$.department.id").value(testDepartment.getId()));
  }
}
