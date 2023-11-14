package matchmaker.backend;

import com.google.firebase.auth.FirebaseAuth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

/**
 * Adds the current logged in user to every request
 */
@Component
@Transactional
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private FirebaseAuth firebaseAuth;

    @Autowired
    Environment env;
    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(("test").equals(env.getProperty("spring.profiles.active"))){
            log.info("[Auth Interceptor] Test environment detected, returning user 1");
            Optional<User> loggedInUser = userRepository.findById(1L);
            if(loggedInUser.isEmpty()){
                log.info("[Auth Interceptor] No matching user found for id 1");
                request.setAttribute("loggedInUser", null);
                return true;
            }
            User existingUser = loggedInUser.get();
            existingUser.getFavorites().stream().count(); // This triggers hiberate to load the (eager) favorite field
            log.info("[Auth Interceptor] Request performed by " + existingUser.name );
            request.setAttribute("loggedInUser", existingUser);
            return true;
        }

        if(request.getUserPrincipal() == null){
            log.info("[Auth Interceptor] No user principal found");
            request.setAttribute("loggedInUser", null);
            return true;
        }
        String firebaseUID = firebaseAuth.getUser(request.getUserPrincipal().getName()).getUid();
        Optional<User> loggedInUser = userRepository.findByFirebaseId(firebaseUID);
        if(loggedInUser.isEmpty()){
            log.info("[Auth Interceptor] No matching user found for firebase id " + firebaseUID);
            request.setAttribute("loggedInUser", null);
            return true;
        }

        User existingUser = loggedInUser.get();
        existingUser.getFavorites().stream().count(); // This triggers hiberate to load the (eager) favorite field
        log.info("[Auth Interceptor] Request performed by " + existingUser.name );
        request.setAttribute("loggedInUser", existingUser);


        return true; // Continue processing the request
    }
}
