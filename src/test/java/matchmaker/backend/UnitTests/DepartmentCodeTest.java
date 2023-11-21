package matchmaker.backend.UnitTests;

import matchmaker.backend.models.Department;
import matchmaker.backend.models.DepartmentCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepartmentCodeTest {

    @Test
    public void testGetCode() {
        DepartmentCode departmentCode = new DepartmentCode();
        departmentCode.code = "AFI34dsdSJ";
        assertEquals(departmentCode.getCode(), "AFI34dsdSJ");
    }

    @Test
    public void testGetDepartment() {
        DepartmentCode departmentCode = new DepartmentCode();
        Department testdepartment = new Department();
        departmentCode.department = testdepartment;
        assertEquals(departmentCode.getDepartment(), testdepartment);
    }

    @Test
    public void testSetCode() {
        DepartmentCode departmentCode = new DepartmentCode();
        departmentCode.setCode("AFI34dsdSJ");
        assertEquals(departmentCode.code, "AFI34dsdSJ");
    }

    @Test
    public void testSetDepartment() {
        DepartmentCode departmentCode = new DepartmentCode();
        Department testdepartment = new Department();
        departmentCode.setDepartment(testdepartment);
        assertEquals(departmentCode.department, testdepartment);
    }
}
