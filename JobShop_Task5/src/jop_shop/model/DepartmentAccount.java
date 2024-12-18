package jop_shop.model;

import java.sql.Date;

public class DepartmentAccount extends Account {
	private int departmentId;

	public DepartmentAccount(String accountNumber, Date establishedDate, double cost) {
		super(accountNumber, establishedDate, cost);
	}

	public DepartmentAccount(String accountNumber, Date establishedDate, double cost, int departmentId) {
		super(accountNumber, establishedDate, cost);
		this.departmentId = departmentId;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
}
