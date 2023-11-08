package matchmaker.backend.IntegrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ChallengeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testChallengesTableNotEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/challenges")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }
    @Test
    public void testGetChallengeById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/challenge/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }
    @Test
    public void testGetChallengeByIdThatDoesntExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/challenge/99999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testSearchByQuery() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/challenge/search?query=Innovatie")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }
    @Test
    public void testSearchByTags() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/challenge/search?tags=prototype")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }
    @Test
    public void testSearchByDescription() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/challenge/search?description=description")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }
    @Test
    public void testSearchBySummary() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/challenge/search?summary=summary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    public void testFetchChallenges() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/challenge/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }
}