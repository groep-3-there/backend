package matchmaker.backend.controllers;

import matchmaker.backend.constants.Perm;
import matchmaker.backend.models.Department;
import matchmaker.backend.models.DepartmentCode;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.DepartmentCodeRepository;
import matchmaker.backend.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Random;

@RestController
public class DepartmentCodeController {

  @Autowired public DepartmentCodeRepository departmentCodeRepository;

  @Autowired public DepartmentRepository departmentRepository;

  @GetMapping("/departmentcode/{DepartmentId}")
  public ResponseEntity<DepartmentCode> getDepartmentCode(
      @PathVariable Long DepartmentId,
      @RequestAttribute("loggedInUser") User currentUser) {

    // check if the user has the permission to see the department code
    if (!currentUser.hasPermissionAtDepartment(Perm.DEPARTMENT_MANAGE, DepartmentId)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    Optional<DepartmentCode> code = departmentCodeRepository.findByDepartmentId(DepartmentId);

    // return code if one exists
    if (code.isPresent()) {
      return ResponseEntity.ok(code.get());
    }

    // check if the department exists
    Optional<Department> department = departmentRepository.findById(DepartmentId);
    if (department.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // create new code
    DepartmentCode newCode = createDepartmentCode(department.get());
    return ResponseEntity.ok(newCode);
  }

  private DepartmentCode createDepartmentCode(Department department) {
    DepartmentCode code = new DepartmentCode();
    code.setDepartment(department);

    // the properties of the randomcode
    int length = 10;
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder randomString = new StringBuilder();

    Random rnd = new Random();
    for (int i = 0; i < length; i++) {
      randomString.append(chars.charAt(rnd.nextInt(chars.length())));
    }

    code.setCode(randomString.toString());
    return departmentCodeRepository.save(code);
  }
}
