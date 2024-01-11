package matchmaker.backend.controllers;

import matchmaker.backend.models.Permission;
import matchmaker.backend.models.Role;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/role/assignable")
    public Iterable<Role> getAssignableRoles() {
        return roleRepository.findAllByIsAssignable(true);
    }

    @GetMapping("/role")
    public Iterable<Role> getRoles() {
        return roleRepository.findAll();
    }
    @GetMapping("/role/{id}")
    public ResponseEntity<Role> getRole(@PathVariable Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(role.get());
    }
    @PostMapping("/role")
    public ResponseEntity<Role> createRole(@RequestBody Role newRole,@RequestAttribute("loggedInUser") User currentUser) {
        if (currentUser == null || currentUser.role == null || !currentUser.role.isMatchmaker) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        newRole.id = null;
        newRole.createdAt = LocalDate.now();
        Role saved = roleRepository.save(newRole);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/role/{id}")
    public ResponseEntity<Role> deleteRole(@PathVariable Long id, @RequestAttribute("loggedInUser") User currentUser) {
        if (currentUser == null || currentUser.role == null || !currentUser.role.isMatchmaker) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        roleRepository.deleteById(id);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/role/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role, @RequestAttribute("loggedInUser") User currentUser) {
        if (currentUser == null || currentUser.role == null || !currentUser.role.isMatchmaker) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Optional<Role> exist = roleRepository.findById(id);
        if (exist.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Role saved = roleRepository.save(role);
        return ResponseEntity.ok(saved);
    }
}
