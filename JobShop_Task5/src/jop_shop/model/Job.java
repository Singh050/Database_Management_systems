package jop_shop.model;

import java.sql.Date;

public class Job {
	protected int jobNumber;
	protected Date completedDate;
	protected Date commencedDate;
	protected int assemblyId;
	protected int processId;
	protected int laborTime;

	public Job(int jobNumber, Date completedDate, Date commencedDate, int assemblyId, int processId, int laborTime) {
		this.jobNumber = jobNumber;
		this.completedDate = completedDate;
		this.commencedDate = commencedDate;
		this.assemblyId = assemblyId;
		this.processId = processId;
		this.laborTime = laborTime;
	}

    public Job(Date completedDate, Date commencedDate, int assemblyId, int processId, int laborTime) {
		this.completedDate = completedDate;
		this.commencedDate = commencedDate;
		this.assemblyId = assemblyId;
		this.processId = processId;
		this.laborTime = laborTime;
    }

	public Job(Date completedDate, Date commencedDate, int processId, int laborTime) {
		this.completedDate = completedDate;
		this.commencedDate = commencedDate;
		this.processId = processId;
		this.laborTime = laborTime;
	}

	public int getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(int jobNumber) {
		this.jobNumber = jobNumber;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public Date getCommencedDate() {
		return commencedDate;
	}

	public void setCommencedDate(Date commencedDate) {
		this.commencedDate = commencedDate;
	}

	public int getAssemblyId() {
		return assemblyId;
	}

	public void setAssemblyId(int assemblyId) {
		this.assemblyId = assemblyId;
	}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public int getLaborTime() {
		return laborTime;
	}

	public void setLaborTime(int laborTime) {
		this.laborTime = laborTime;
	}

	@Override
	public String toString() {
		return "Job{" +
				"jobNumber=" + jobNumber +
				", completedDate=" + completedDate +
				", commencedDate=" + commencedDate +
				", assemblyId=" + assemblyId +
				", processId=" + processId +
				", laborTime=" + laborTime +
				'}';
	}
}
