package matchmaker.backend;

import org.junit.jupiter.api.Test;
import matchmaker.backend.models.User;
public class UserTest {
    @Test
    public void getCorrectUserName() {
        User user = new User("test");
        assert user.getName().equals("test");
    }
}
