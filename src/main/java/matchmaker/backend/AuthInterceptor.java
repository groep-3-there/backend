package matchmaker.backend;

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

/**
 * Adds the current logged in user to every request
 */
@Component
@Transactional
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //TODO make this depend on the current session.
        User loggedInUser = userRepository.findById(1L).get();
        loggedInUser.getFavorites().stream().count(); // This triggers hiberate to load the (eager) favorite field
        request.setAttribute("loggedInUser", loggedInUser);

        log.info("[Auth Interceptor] Request performed by " + loggedInUser.name);
        return true; // Continue processing the request
    }
}
