package matchmaker.backend.controllers;

import matchmaker.backend.constants.Perm;
import matchmaker.backend.models.Department;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
public class DepartmentController {
  @Autowired private DepartmentRepository departmentRepository;

  @PostMapping("/department/create")
  public ResponseEntity createDepartment(
      @RequestBody Department department, @RequestAttribute("loggedInUser") User currentUser) {
    // Check if the loggedInUser can create a department
    if (!currentUser.hasPermissionAtDepartment(Perm.DEPARTMENT_CREATE, department.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    // User has permission.
    // Only copy the name, since the parentcompany will be set by the users department, same with
    // the ID and created at.
    department.setId(null);
    department.setParentCompany(currentUser.getDepartment().getParentCompany());
    department.setCreatedAt(new Date());
    // Save the department
    departmentRepository.save(department);
    return ResponseEntity.status(HttpStatus.OK).body("Department created");
  }

  @GetMapping("/department/company/{id}")
  public Iterable<Department> getAllDepartmentsForCompanyById(@PathVariable("id") Long id) {
    // Get all the departments where partenCompany is equal to the id
    return departmentRepository.findAllByParentCompanyId(id);
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
