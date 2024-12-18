package jop_shop.model;

import java.sql.Date;

public class ProcessAccount extends Account {
	private int processId;

	public ProcessAccount(String accountNumber, Date establishedDate, double cost) {
		super(accountNumber, establishedDate, cost);
	}

	public ProcessAccount(String accountNumber, Date establishedDate, double cost, int processId) {
		super(accountNumber, establishedDate, cost);
		this.processId = processId;
	}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}
}
