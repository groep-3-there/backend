package matchmaker.backend.UnitTests;

import matchmaker.backend.models.Branch;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BranchTest {

    @Test
    void getId() {
        Branch testBranch = new Branch();
        testBranch.id = 1L;
        assertEquals(testBranch.getId(), 1L);
    }

    @Test
    void getName() {
        Branch testBranch = new Branch();
        testBranch.name = "testName";
        assertEquals(testBranch.getName(), "testName");
    }

    @Test
    void setId() {
        Branch testBranch = new Branch();
        testBranch.setId(1L);
        assertEquals(testBranch.id, 1L);
    }

    @Test
    void setName() {
        Branch testBranch = new Branch();
        testBranch.setName("testName");
        assertEquals(testBranch.name, "testName");
    }
}
