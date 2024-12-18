package jop_shop.model;

import java.sql.Date;

public class AssemblyAccount extends Account {
	private int assemblyId;

	public AssemblyAccount(String accountNumber, Date establishedDate, double cost) {
		super(accountNumber, establishedDate, cost);
	}

	public AssemblyAccount(String accountNumber, Date establishedDate, double cost, int assemblyId) {
		super(accountNumber, establishedDate, cost);
		this.assemblyId = assemblyId;
	}

	public int getAssemblyId() {
		return assemblyId;
	}

	public void setAssemblyId(int assemblyId) {
		this.assemblyId = assemblyId;
	}
}
