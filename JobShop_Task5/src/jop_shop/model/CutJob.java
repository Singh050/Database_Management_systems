package jop_shop.model;

import java.sql.Date;

public class CutJob extends Job {
	private String machineType;
	private int machineUsedTime;
	private String materialUsed;

	public CutJob(Date completedDate, Date commencedDate, int assemblyId, int processId, int laborTime, String machineType, int machineUsedTime, String materialUsed) {
		super(completedDate, commencedDate, assemblyId, processId, laborTime);
		this.machineType = machineType;
		this.machineUsedTime = machineUsedTime;
		this.materialUsed = materialUsed;
	}

	public CutJob(Date completedDate, Date commencedDate, int processId, int laborTime, String machineType, int machineUsedTime, String materialUsed) {
		super(completedDate, commencedDate, processId, laborTime);
		this.machineType = machineType;
		this.machineUsedTime = machineUsedTime;
		this.materialUsed = materialUsed;
	}

	public CutJob(int jobNumber, Date completedDate, Date commencedDate, int assemblyId, int processId, int laborTime, String machineType, int machineUsedTime, String materialUsed) {
		super(jobNumber, completedDate, commencedDate, assemblyId, processId, laborTime);
		this.machineType = machineType;
		this.machineUsedTime = machineUsedTime;
		this.materialUsed = materialUsed;
	}

	public String getMachineType() {
		return machineType;
	}

	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}

	public int getMachineUsedTime() {
		return machineUsedTime;
	}

	public void setMachineUsedTime(int machineUsedTime) {
		this.machineUsedTime = machineUsedTime;
	}

	public String getMaterialUsed() {
		return materialUsed;
	}

	public void setMaterialUsed(String materialUsed) {
		this.materialUsed = materialUsed;
	}

	@Override
	public String toString() {
		return "CutJob{" +
				"machineType='" + machineType + '\'' +
				", machineUsedTime=" + machineUsedTime +
				", materialUsed='" + materialUsed + '\'' +
				", jobNumber=" + jobNumber +
				", completedDate=" + completedDate +
				", commencedDate=" + commencedDate +
				", assemblyId=" + assemblyId +
				", processId=" + processId +
				", laborTime=" + laborTime +
				'}';
	}
}
