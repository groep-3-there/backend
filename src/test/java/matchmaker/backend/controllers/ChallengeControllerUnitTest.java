package matchmaker.backend.controllers;

import matchmaker.backend.models.Challenge;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.ChallengeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class ChallengeControllerUnitTest {

    @Autowired
    private ChallengeRepository challengeRepository;
    private final User user = new User("test");

    @Test
    public void testChallengesTableNotEmpty() throws Exception {
        ChallengeController challengeController = new ChallengeController(challengeRepository);
        Iterable<Challenge> challenges = challengeController.getChallenges();
        Assertions.assertThat(challenges).isNotEmpty();
    }
    @Test
    public void testGetChallengeById() throws Exception {
        ChallengeController challengeController = new ChallengeController(challengeRepository);
        Challenge challenge = challengeController.getChallengeById(1L, user).getBody();
        Assertions.assertThat(challenge).isNotNull();
    }
    @Test
    public void testGetChallengeByIdThatDoesntExist() throws Exception {
        ChallengeController challengeController = new ChallengeController(challengeRepository);
        Challenge challenge = challengeController.getChallengeById(99999L, user).getBody();
        Assertions.assertThat(challenge).isNull();
    }
    @Test
    public void testSearchByQuery() throws Exception {
        ChallengeController challengeController = new ChallengeController(challengeRepository);
        Iterable<Challenge> challenges = challengeController.search("Innovatie", null, null, null,0);
        Assertions.assertThat(challenges).anyMatch(challenge -> challenge.id.equals(1L));
    }
    @Test
    public void testSearchByTags() throws Exception {
        ChallengeController challengeController = new ChallengeController(challengeRepository);
        Iterable<Challenge> challenges = challengeController.search("prototype", null, null, null, 0);
        Assertions.assertThat(challenges).anyMatch(challenge -> challenge.id.equals(1L));
    }
    @Test
    public void testSearchByDescription() throws Exception {
        ChallengeController challengeController = new ChallengeController(challengeRepository);
        Iterable<Challenge> challenges = challengeController.search("challenge description", null, null, null, 0);
        Assertions.assertThat(challenges).anyMatch(challenge -> challenge.id.equals(1L));
    }
    @Test
    public void testSearchBySummary() throws Exception {
        ChallengeController challengeController = new ChallengeController(challengeRepository);
        Iterable<Challenge> challenges = challengeController.search("Summary", null, null, null, 0);
        Assertions.assertThat(challenges).anyMatch(challenge -> challenge.id.equals(1L));
    }
    @Test
    public void testSearchByCompany() throws Exception {
        ChallengeController challengeController = new ChallengeController(challengeRepository);
        ArrayList<String> companyList = new ArrayList<String>();
        companyList.add("ChipSoft");
        Iterable<Challenge> challenges = challengeController.search(null, companyList , null, null, 0);
        Assertions.assertThat(challenges).anyMatch(challenge -> challenge.id.equals(1L));
    }
    @Test
    public void faultyTest() throws Exception {
        ChallengeController challengeController = new ChallengeController(challengeRepository);
        ArrayList<String> companyList = new ArrayList<String>();
        companyList.add("testcompany");
        Iterable<Challenge> challenges = challengeController.search(null, companyList , null, null, 0);
        Assertions.assertThat(challenges).anyMatch(challenge -> challenge.id.equals(1L));
    }
}
