package matchmaker.backend;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.Optional;

/** Adds the current logged in user to every request */
@Component
@Transactional
public class AuthInterceptor implements HandlerInterceptor {
  @Autowired UserRepository userRepository;

  @Autowired private FirebaseAuth firebaseAuth;

  @Autowired private FirebaseApp firebaseApp;

  @Autowired private Environment environment;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (request.getUserPrincipal() == null) {
      if (Objects.equals(environment.getProperty("spring.profiles.active"), "test")
          && userRepository.existsById(1L)) {
        User testUser = userRepository.findById(1L).get();
        if (request.getParameter("loggedOut") != null) {
          request.setAttribute("loggedInUser", null);
          return true;
        }
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
      request.setAttribute("loggedInUser", null);
      return true;
    }

    User existingUser = loggedInUser.get();
    request.setAttribute("loggedInUser", existingUser);

    return true; // Continue processing the request
  }
}
