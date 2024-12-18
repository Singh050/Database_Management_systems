package jop_shop.model;

import java.sql.Date;

public class Account {
	protected String accountNumber;
	protected Date establishedDate;
	protected double cost;

	public Account(String accountNumber, Date establishedDate, double cost) {
		this.accountNumber = accountNumber;
		this.establishedDate = establishedDate;
		this.cost = cost;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Date getEstablishedDate() {
		return establishedDate;
	}

	public void setEstablishedDate(Date establishedDate) {
		this.establishedDate = establishedDate;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
}
