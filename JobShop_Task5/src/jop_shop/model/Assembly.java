package jop_shop.model;

import java.sql.Date;

public class Assembly {
	private int assemblyId;
	private Date dateOrdered;
	private String assemblyDetail;
	private int customerId;

	public Assembly() {
	}

	public Assembly(int assemblyId, Date dateOrdered, String assemblyDetail, int customerId) {
		this.assemblyId = assemblyId;
		this.dateOrdered = dateOrdered;
		this.assemblyDetail = assemblyDetail;
		this.customerId = customerId;
	}

	public Assembly(Date dateOrdered, String assemblyDetail, int customerId) {
		this.dateOrdered = dateOrdered;
		this.assemblyDetail = assemblyDetail;
		this.customerId = customerId;
	}

	public int getAssemblyId() {
		return assemblyId;
	}

	public void setAssemblyId(int assemblyId) {
		this.assemblyId = assemblyId;
	}

	public Date getDateOrdered() {
		return dateOrdered;
	}

	public void setDateOrdered(Date dateOrdered) {
		this.dateOrdered = dateOrdered;
	}

	public String getAssemblyDetail() {
		return assemblyDetail;
	}

	public void setAssemblyDetail(String assemblyDetail) {
		this.assemblyDetail = assemblyDetail;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	@Override
	public String toString() {
		return "Assembly{" +
				"assemblyId=" + assemblyId +
				", dateOrdered=" + dateOrdered +
				", assemblyDetail='" + assemblyDetail + '\'' +
				", customerId=" + customerId +
				'}';
	}
}
