package matchmaker.backend.database;

import matchmaker.backend.models.Challenge;
import matchmaker.backend.models.Company;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.ChallengeRepository;
import matchmaker.backend.repositories.CompanyRepository;
import matchmaker.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class LoadDatabase implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    private UserRepository users;
    private ChallengeRepository challenges;
    private CompanyRepository companies;

    public User Florijn = new User("Florijn");
    public User Luke = new User("Luke");

    @Autowired
    public void DataLoader(UserRepository userRepository, ChallengeRepository challengeRepository, CompanyRepository companyRepository) {
        this.users = userRepository;
        this.challenges = challengeRepository;
        this.companies = companyRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Preloading " + users.save(Florijn));
        log.info("Preloading " + users.save(Luke));
        log.info("Preloading " + challenges.save(new Challenge("Challenge 1", Florijn)));
        log.info("Preloading " + challenges.save(new Challenge("Challenge 2", Luke)));
        log.info("Preloading " + companies.save(new Company("There", Florijn)));
        log.info("Preloading " + companies.save( new Company("MatchMaker", Luke)));
    }
}