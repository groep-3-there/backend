package matchmaker.backend.controllers;

import matchmaker.backend.constants.DefaultRoleId;
import matchmaker.backend.constants.Perm;
import matchmaker.backend.models.Role;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import matchmaker.backend.constants.DefaultRoleId;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class RoleController {
    @Autowired private RoleRepository roleRepository;

    @GetMapping("/role/assignable")
    public Iterable<Role> getAssignableRoles() {
        return roleRepository.findAllByIsAssignable(true);
    }



}
