package matchmaker.backend.database;

import matchmaker.backend.repositories.PermissionRepository;
import matchmaker.backend.repositories.RoleRepository;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
public class LoadDatabase implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!testDataExists()) {
            loadTestData();
        }
    }
    private boolean testDataExists() {
        String query = "SELECT COUNT(*) FROM permissions WHERE code_name = 'CHALLENGE_READ'";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class);
        return count != null && count > 0;
    }

    private void loadTestData() {
        try {
            Resource resource = new ClassPathResource("testdata/generate_testdata.sql");
            String sql = StreamUtils.copyToString(resource.getInputStream(), java.nio.charset.StandardCharsets.UTF_8);
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            System.out.println("Error loading test data: " + e.getMessage());
        }
    }
}