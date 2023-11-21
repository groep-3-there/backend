package matchmaker.backend.UnitTests;

import matchmaker.backend.models.Department;
import matchmaker.backend.models.Permission;
import matchmaker.backend.models.Role;
import matchmaker.backend.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
  @Test
  void hasPermissionAtDepartment() {
    Role testRole = new Role("testRole");
    testRole.isMatchmaker = false;
    Department testDepartment = new Department(1L, "testDepartment", null, null);
    Permission testPermission = new Permission("testPermission", "test", "test");
    testRole.permissions.add(testPermission);
    String permissionToTest = "testPermission";
    User testUser = new User("testUser");
    testUser.setRole(testRole);
    testUser.setDepartment(testDepartment);
    assert (testUser.hasPermissionAtDepartment(permissionToTest, 1L));
  }

  @Test
  void isInCompany() {
    Department testDepartment = new Department(1L, "testDepartment", null, null);
    User testUser = new User("testUser");
    testUser.setDepartment(testDepartment);
    assert (testUser.isInCompany());
  }

  @Test
  void getRole() {
    Role testRole = new Role("testRole");
    User testUser = new User("testUser");
    testUser.setRole(testRole);
    assert (testUser.getRole().equals(testRole));
  }

  @Test
  void getDepartment() {
    Department testDepartment = new Department(1L, "testDepartment", null, null);
    User testUser = new User("testUser");
    testUser.setDepartment(testDepartment);
    assert (testUser.getDepartment().equals(testDepartment));
  }

  @Test
  void setRole() {
    Role testRole = new Role("testRole");
    User testUser = new User("testUser");
    testUser.setRole(testRole);
    assert (testUser.getRole().equals(testRole));
  }

  @Test
  void setDepartment() {
    Department testDepartment = new Department(1L, "testDepartment", null, null);
    User testUser = new User("testUser");
    testUser.setDepartment(testDepartment);
    assert (testUser.getDepartment().equals(testDepartment));
  }
}
