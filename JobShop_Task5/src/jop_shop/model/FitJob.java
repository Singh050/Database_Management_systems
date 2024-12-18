package jop_shop.model;

import java.sql.Date;

public class FitJob extends Job {
	public FitJob(int jobNumber, Date completedDate, Date commencedDate, int assemblyId, int processId, int laborTime) {
		super(jobNumber, completedDate, commencedDate, assemblyId, processId, laborTime);
	}

	public FitJob(Date completedDate, Date commencedDate, int assemblyId, int processId, int laborTime) {
		super(completedDate, commencedDate, assemblyId, processId, laborTime);
	}

	public FitJob(Date completedDate, Date commencedDate, int processId, int laborTime) {
		super(completedDate, commencedDate, processId, laborTime);
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
