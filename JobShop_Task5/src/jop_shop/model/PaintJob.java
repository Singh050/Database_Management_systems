package jop_shop.model;

import java.sql.Date;

public class PaintJob extends Job {
	private String color;
	private int volume;

	public PaintJob(Date completedDate, Date commencedDate, int assemblyId, int processId, int laborTime, String color, int volume) {
		super(completedDate, commencedDate, assemblyId, processId, laborTime);
		this.color = color;
		this.volume = volume;
	}

	public PaintJob(Date completedDate, Date commencedDate, int processId, int laborTime, String color, int volume) {
		super(completedDate, commencedDate, processId, laborTime);
		this.color = color;
		this.volume = volume;
	}

	public PaintJob(int jobNumber, Date completedDate, Date commencedDate, int assemblyId, int processId, int laborTime, String color, int volume) {
		super(jobNumber, completedDate, commencedDate, assemblyId, processId, laborTime);
		this.color = color;
		this.volume = volume;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {
		return "PaintJob{" +
				"color='" + color + '\'' +
				", volume=" + volume +
				", jobNumber=" + jobNumber +
				", completedDate=" + completedDate +
				", commencedDate=" + commencedDate +
				", assemblyId=" + assemblyId +
				", processId=" + processId +
				", laborTime=" + laborTime +
				'}';
	}
}
