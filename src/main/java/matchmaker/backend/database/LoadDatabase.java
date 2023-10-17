package matchmaker.backend.database;

import matchmaker.backend.models.Challenge;
import matchmaker.backend.models.Company;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.ChallengeRepository;
import matchmaker.backend.repositories.CompanyRepository;
import matchmaker.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    public User Florijn = new User("Florijn");
    public User Luke = new User("Luke");

    @Bean
    CommandLineRunner initDatabase(UserRepository users, ChallengeRepository challenges, CompanyRepository companies) {

        return args -> {
            log.info("Preloading " + users.save(Florijn));
            log.info("Preloading " + users.save(Luke));
            log.info("Preloading " + challenges.save(new Challenge("Challenge 1", Florijn.getUUID())));
            log.info("Preloading " + challenges.save(new Challenge("Challenge 2", Luke.getUUID())));
            log.info("Preloading " + companies.save(new Company("There", Florijn.getUUID())));
            log.info("Preloading " + companies.save( new Company("MatchMaker", Luke.getUUID())));
        };
    }
}