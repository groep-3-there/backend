package matchmaker.backend.RequestBodies;

public class CreateDepartmentFields {
  public String name;
  public Long adminId;

  public CreateDepartmentFields(String name, Long adminId) {
    this.name = name;
    this.adminId = adminId;
  }
}
