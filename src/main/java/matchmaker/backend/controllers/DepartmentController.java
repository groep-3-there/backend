package matchmaker.backend.controllers;

import matchmaker.backend.RequestBodies.CreateDepartmentFields;
import matchmaker.backend.RequestBodies.SearchDepartmentFields;
import matchmaker.backend.constants.Perm;
import matchmaker.backend.models.Department;
import matchmaker.backend.models.Role;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.DepartmentRepository;
import matchmaker.backend.repositories.RoleRepository;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
public class DepartmentController {
  @Autowired private DepartmentRepository departmentRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private RoleRepository roleRepository;

  @PostMapping("/department/create")
  public ResponseEntity<Department> createDepartment(
      @RequestBody CreateDepartmentFields fields,
      @RequestAttribute("loggedInUser") User currentUser) {
    if (currentUser == null || !currentUser.isInCompany()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    if (!currentUser.hasPermissionAtDepartment(
        Perm.DEPARTMENT_CREATE, currentUser.department.getId())) {
      // User does not have permission to create department
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    if (userRepository.findById(fields.adminId).isEmpty()) {
      // target admin does not exist
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    User targetDepartmentAdmin = userRepository.findById(fields.adminId).get();

    if (!targetDepartmentAdmin.isInCompany()) {
      // target admin is not in a company
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    if (!targetDepartmentAdmin.department.parentCompany.id.equals(
        currentUser.department.parentCompany.id)) {
      // user is not in the same company
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    if (targetDepartmentAdmin.role.isDepartmentAdmin) {
      // target admin is already a department admin
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    // User has permission.
    Department newDepartment = new Department();
    newDepartment.setName(fields.name);
    newDepartment.setParentCompany(currentUser.getDepartment().getParentCompany());
    newDepartment.setCreatedAt(new Date());
    // Save the department
    Department saved = departmentRepository.save(newDepartment);
    Optional<Role> departmentAdmin = roleRepository.findById(3L);
    if (departmentAdmin.isEmpty()) {
      // If this happens, the role is not in the database, check testdata
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    targetDepartmentAdmin.setDepartment(saved);
    targetDepartmentAdmin.setRole(departmentAdmin.get());
    userRepository.save(targetDepartmentAdmin);

    return ResponseEntity.status(HttpStatus.OK).body(saved);
  }

  @GetMapping("/department/company/{id}")
  public Iterable<Department> getAllDepartmentsForCompanyById(@PathVariable("id") Long id) {
    // Get all the departments where partenCompany is equal to the id
    return departmentRepository.findAllByParentCompanyId(id);
  }

  @PostMapping("/department/exists")
  public ResponseEntity<Optional<Department>> departmentExists(
      @RequestBody SearchDepartmentFields fields) {
    // Check if the department exists

    return ResponseEntity.ok(
        departmentRepository.findByNameAndParentCompanyId(fields.name, fields.parentCompanyId));
  }

  @GetMapping("/department/{id}")
  public ResponseEntity getDepartmentById(@PathVariable("id") Long id) {
    Optional<Department> optionalDepartment = departmentRepository.findById(id);
    if (optionalDepartment.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    return ResponseEntity.status(HttpStatus.OK).body(optionalDepartment.get());
  }
}
