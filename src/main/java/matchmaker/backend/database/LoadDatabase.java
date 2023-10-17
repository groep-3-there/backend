package matchmaker.backend.database;

import matchmaker.backend.models.User;
import matchmaker.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new User("Florijn")));
            log.info("Preloading " + repository.save(new User("Luke")));
            log.info("Preloading " + repository.save(new User("Rik")));
            log.info("Preloading " + repository.save(new User("Tjerk")));
            log.info("Preloading " + repository.save(new User("Eelco")));
            log.info("Preloading " + repository.save(new User("Jelle")));
        };
    }
}