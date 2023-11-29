package matchmaker.backend.controllers;

import matchmaker.backend.RequestBodies.CreateDepartmentFields;
import matchmaker.backend.RequestBodies.SearchDepartmentFields;
import matchmaker.backend.constants.DefaultRoleId;
import matchmaker.backend.constants.Perm;
import matchmaker.backend.models.Department;
import matchmaker.backend.models.DepartmentCode;
import matchmaker.backend.models.Role;
import matchmaker.backend.models.User;
import matchmaker.backend.repositories.DepartmentCodeRepository;
import matchmaker.backend.repositories.DepartmentRepository;
import matchmaker.backend.repositories.RoleRepository;
import matchmaker.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class DepartmentController {
  @Autowired private DepartmentRepository departmentRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private RoleRepository roleRepository;
  @Autowired private DepartmentCodeRepository departmentCodeRepository;

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

  @GetMapping("/department/code/{code}")
  public ResponseEntity<Department> getDepartmentByCode(@PathVariable("code") String code) {
    Optional<DepartmentCode> optionalDepartmentCode = departmentCodeRepository.findByCode(code);
    if (optionalDepartmentCode.isEmpty()) {
      System.out.print("Department code not found");
      System.out.println(code);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    return ResponseEntity.status(HttpStatus.OK).body(optionalDepartmentCode.get().department);
  }

  @GetMapping("/department/{id}")
  public ResponseEntity getDepartmentById(@PathVariable("id") Long id) {
    Optional<Department> optionalDepartment = departmentRepository.findById(id);
    if (optionalDepartment.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    return ResponseEntity.status(HttpStatus.OK).body(optionalDepartment.get());
  }

  @PostMapping("/department/join/{code}")
  public ResponseEntity<Department> joinDepartment(
      @PathVariable("code") String code, @RequestAttribute("loggedInUser") User currentUser) {
    if (currentUser == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    if (currentUser.isInCompany()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    Optional<DepartmentCode> optionalDepartmentCode = departmentCodeRepository.findByCode(code);
    if (optionalDepartmentCode.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    DepartmentCode departmentCode = optionalDepartmentCode.get();
    if (departmentCode.department == null) {
      // should not happen
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    Optional<Role> optionalRole = roleRepository.findById(DefaultRoleId.MEDEWERKER);
    if (optionalRole.isEmpty()) {
      // should not happen
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    currentUser.setDepartment(departmentCode.department);
    currentUser.setRole(optionalRole.get());
    userRepository.save(currentUser);

    return ResponseEntity.status(HttpStatus.OK).body(departmentCode.department);
  }

  @GetMapping("/department/{id}/members")
  public Iterable<User> getUsersinDepartment(@PathVariable("id") Long id) {
    // Get all the users in the department
    return userRepository.findAllByDepartment_Id(id);
  }

  @PutMapping("/department/{id}/updateroles")
  public ResponseEntity<Role> updateRoles(
      @PathVariable("id") Long departmentId,
      @RequestBody Map<String, List<Map<String, Long>>> requestMap,
      @RequestAttribute("loggedInUser") User currentUser) {
    // check if department exists
    Optional<Department> department = departmentRepository.findById(departmentId);
    if (department.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // check if user is logged in and in a company
    if (currentUser == null || !currentUser.isInCompany()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    // check if user has the permission to update the roles
    if (!currentUser.hasPermissionAtDepartment(Perm.DEPARTMENT_MANAGE, departmentId)
        || !currentUser.hasPermissionAtDepartment(Perm.COMPANY_MANAGE, departmentId)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    List<Map<String, Long>> updates = requestMap.get("updates");

    if (updates == null) {
      return ResponseEntity.badRequest().body(null);
    }

    Iterable<User> users = userRepository.findAllByDepartment_Id(departmentId);
    boolean hasDepartmentAdmin = false;

    // check if there is a department admin in the updates
    for (Map<String, Long> toUpdateUser : updates) {
      Optional<User> tempUser = userRepository.findById(toUpdateUser.get("userId"));
      if (tempUser.isEmpty()) {
        return ResponseEntity.badRequest().body(null);
      }
      if (tempUser.get().role.id.equals(DefaultRoleId.DEPARTMENT_BEHEERDER)) {
        hasDepartmentAdmin = true;
        break;
      }
    }

    // check if there is a department admin in the department already
    if (!hasDepartmentAdmin) {
      for (User user : users) {
        if (user.role.id.equals(DefaultRoleId.DEPARTMENT_BEHEERDER)
            && updates.stream().noneMatch(update -> update.get("userId").equals(user.id))) {
          hasDepartmentAdmin = true;
          break;
        }
      }
    }

    for (Map<String, Long> update : updates) {
      if (update.get("userId") == null || update.get("roleId") == null) {
        return ResponseEntity.badRequest().body(null);
      }
      Optional<User> user = userRepository.findById(update.get("userId"));
      Optional<Role> role = roleRepository.findById(update.get("roleId"));
      if (user.isEmpty() || role.isEmpty()) {
        return ResponseEntity.badRequest().body(null);
      }
      if (!user.get().department.id.equals(departmentId)) {
        return ResponseEntity.badRequest().body(null);
      }
      user.get().setRole(role.get());
      userRepository.save(user.get());
    }
    return ResponseEntity.ok().body(null);
  }
}
