package jop_shop.model;

public class Department {
	private int departmentId;
	private String departmentData;

	public Department() {
	}

	public Department(int departmentId, String departmentData) {
		this.departmentId = departmentId;
		this.departmentData = departmentData;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentData() {
		return departmentData;
	}

	public void setDepartmentData(String departmentData) {
		this.departmentData = departmentData;
	}
}
