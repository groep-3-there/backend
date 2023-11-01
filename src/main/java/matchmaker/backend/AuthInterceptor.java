package matchmaker.backend;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Adds the current logged in user to every request
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    UserRepository userRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //TODO make this depend on the current session.
        User loggedInUser = userRepository.findById(1L).get();

        request.setAttribute("loggedInUser", loggedInUser);

        return true; // Continue processing the request
    }
}
