package matchmaker.backend;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

/** Adds the current logged in user to every request */
@Component
@Transactional
public class AuthInterceptor implements HandlerInterceptor {
  @Autowired UserRepository userRepository;

  @Autowired private FirebaseAuth firebaseAuth;

  @Autowired private FirebaseApp firebaseApp;

  @Autowired private Environment environment;
  private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (request.getUserPrincipal() == null) {
      log.info("[Auth Interceptor] No user principal found");
      if (environment.getProperty("spring.profiles.active").equals("test")
          && userRepository.existsById(1L)) {
        User testUser = userRepository.findById(1L).get();
        request.setAttribute("loggedInUser", testUser);
        return true;
      } else {
        request.setAttribute("loggedInUser", null);
        return true;
      }
    }
    String firebaseUID = firebaseAuth.getUser(request.getUserPrincipal().getName()).getUid();
    Optional<User> loggedInUser = userRepository.findByFirebaseId(firebaseUID);
    if (loggedInUser.isEmpty()) {
      log.info("[Auth Interceptor] No matching user found for firebase id " + firebaseUID);
      request.setAttribute("loggedInUser", null);
      return true;
    }

    User existingUser = loggedInUser.get();
    log.info("[Auth Interceptor] Request performed by " + existingUser.name);
    request.setAttribute("loggedInUser", existingUser);

    return true; // Continue processing the request
  }
}
