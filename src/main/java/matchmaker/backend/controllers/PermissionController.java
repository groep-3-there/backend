package matchmaker.backend.controllers;

import matchmaker.backend.models.Permission;
import matchmaker.backend.models.Role;
import matchmaker.backend.repositories.PermissionRepository;
import matchmaker.backend.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PermissionController {
    @Autowired
    private PermissionRepository permissionRepository;

    @GetMapping("/permission")
    public Iterable<Permission> getPermissions() {
        return permissionRepository.findAll();
    }

}
