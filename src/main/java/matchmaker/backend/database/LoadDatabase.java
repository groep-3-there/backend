package matchmaker.backend.database;

import matchmaker.backend.models.Challenge;
import matchmaker.backend.models.Company;
import matchmaker.backend.models.Image;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.ChallengeRepository;
import matchmaker.backend.repositories.CompanyRepository;
import matchmaker.backend.repositories.ImageRepository;
import matchmaker.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.util.Base64;

@Component
public class LoadDatabase implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    private UserRepository users;
    private ChallengeRepository challenges;
    private CompanyRepository companies;
    private ImageRepository images;

    public User Florijn = new User("Florijn");
    public User Luke = new User("Luke");

    @Autowired
    public void DataLoader(UserRepository userRepository, ChallengeRepository challengeRepository, CompanyRepository companyRepository, ImageRepository imageRepository){
        this.users = userRepository;
        this.challenges = challengeRepository;
        this.companies = companyRepository;
        this.images = imageRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Preloading " + users.save(Florijn));
        log.info("Preloading " + users.save(Luke));
        log.info("Preloading " + challenges.save(new Challenge("Challenge 1", Florijn)));
        log.info("Preloading " + challenges.save(new Challenge("Challenge 2", Luke)));
        log.info("Preloading " + companies.save(new Company("There", Florijn)));
        log.info("Preloading " + companies.save( new Company("MatchMaker", Luke)));
        ClassPathResource resource = new ClassPathResource("image.jpg");
        String encodedString = Base64.getEncoder().encodeToString(resource.getContentAsByteArray());
        System.out.println(encodedString);
        log.info("Preloading " + images.save(new Image(encodedString, "Florijns profiel foto")));
    }
}