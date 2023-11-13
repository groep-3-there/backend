package matchmaker.backend.IntegrationTests;

import matchmaker.backend.models.Company;
import matchmaker.backend.repositories.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class CompanyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private CompanyRepository companyRepository;

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
        mockMvc.perform(MockMvcRequestBuilders.get("/company/" + matchmaker.getId()))
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
