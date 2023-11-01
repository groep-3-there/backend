package matchmaker.backend.database;

import matchmaker.backend.constants.ChallengeStatus;
import matchmaker.backend.models.*;
import matchmaker.backend.repositories.*;
import org.aspectj.apache.bcel.util.ClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.Base64;

@Component
public class LoadDatabase implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    private UserRepository users;
    private ChallengeRepository challenges;
    private CompanyRepository companies;
    private ImageRepository images;
    private RoleRepository roles;
    private PermissionRepository permissions;
    private DepartmentRepository departments;

    public Company chippie = new Company("Chippie");
    public Department chippieIct = new Department("Chip Ict", chippie);
    public Role ChipMedewerker = new Role("Medewerker", chippie, chippieIct);
    public Role ChipBeheerder = new Role("Afdeling Beheerder", chippie, chippieIct);

    public Permission readChallenge = new Permission("READ_CHALLENGE", "Het bekijken van een challenge", "Challenge bekijken");


    public User Florijn = new User("Florijn");

    public User Luke = new User("Luke");

    public LoadDatabase() throws IOException {
    }

    @Autowired
    public void DataLoader(UserRepository userRepository, ChallengeRepository challengeRepository, CompanyRepository companyRepository, ImageRepository imageRepository, RoleRepository roleRepository, PermissionRepository permissionRepository, DepartmentRepository departmentRepository){
        this.users = userRepository;
        this.challenges = challengeRepository;
        this.companies = companyRepository;
        this.images = imageRepository;
        this.roles = roleRepository;
        this.permissions = permissionRepository;
        this.departments = departmentRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if(users.findAll().iterator().hasNext()){
            return;
        }

        companies.save(chippie);
        departments.save(chippieIct);
        permissions.save(readChallenge);
        roles.save(ChipMedewerker);
        ChipMedewerker.permissions.add(readChallenge);
        ChipBeheerder.permissions.add(readChallenge);
        roles.save(ChipMedewerker);

        roles.save(ChipBeheerder);
        Florijn.setRole(ChipMedewerker);
        Luke.setRole(ChipBeheerder);
        log.info("Preloading " + users.save(Florijn));
        log.info("Preloading " + users.save(Luke));

        Challenge a = new Challenge("Challenge 1", Florijn);
        a.status = ChallengeStatus.IN_UITVOERING;
        log.info("Preloading " + challenges.save(a));

        log.info("Preloading " + challenges.save(new Challenge("Challenge 2", Luke)));
        log.info("Preloading " + companies.save(new Company("There", Florijn)));
        log.info("Preloading " + companies.save( new Company("MatchMaker", Luke)));
    }




}