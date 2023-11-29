package matchmaker.backend.controllers;

import matchmaker.backend.models.Role;
import matchmaker.backend.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoleController {
  @Autowired private RoleRepository roleRepository;

  @GetMapping("/role/assignable")
  public Iterable<Role> getAssignableRoles() {
    return roleRepository.findAllByIsAssignable(true);
  }
}
