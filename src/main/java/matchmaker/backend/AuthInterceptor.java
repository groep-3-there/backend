package matchmaker.backend;

import com.google.firebase.auth.FirebaseAuth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(request.getUserPrincipal() == null){
            log.info("No user principal found");
            request.setAttribute("loggedInUser", null);
            return true;
        }
        String firebaseUID = firebaseAuth.getUser(request.getUserPrincipal().getName()).getUid();
        Optional<User> loggedInUser = userRepository.findByFirebaseId(firebaseUID);
        if(loggedInUser.isEmpty()){
            log.info("No matching user found for firebase id " + firebaseUID);
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
