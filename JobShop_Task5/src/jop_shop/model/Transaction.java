package jop_shop.model;

public class Transaction {
	private int transactionNumber;
	private double suppliedCost;
	private String accountNumber;
	private int jobNumber;

	public Transaction() {
	}

	public Transaction(double suppliedCost, String accountNumber, int jobNumber) {
		this.suppliedCost = suppliedCost;
		this.accountNumber = accountNumber;
		this.jobNumber = jobNumber;
	}

	public Transaction(int transactionNumber, double suppliedCost, String accountNumber, int jobNumber) {
		this.transactionNumber = transactionNumber;
		this.suppliedCost = suppliedCost;
		this.accountNumber = accountNumber;
		this.jobNumber = jobNumber;
	}

	public int getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(int transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public double getSuppliedCost() {
		return suppliedCost;
	}

	public void setSuppliedCost(double suppliedCost) {
		this.suppliedCost = suppliedCost;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(int jobNumber) {
		this.jobNumber = jobNumber;
	}
}
