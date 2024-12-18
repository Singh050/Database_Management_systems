package jop_shop.model;

public class PaintProcess extends Process {
	private String paintType;
	private String paintMethod;

	public PaintProcess( String processData, int departmentId) {
		super(processData, departmentId);
	}

	public PaintProcess(int processId, String processData, int departmentId, String paintType, String paintMethod) {
		super(processId, processData, departmentId);
		this.paintType = paintType;
		this.paintMethod = paintMethod;
	}

	public PaintProcess(String processData, int departmentId, String paintType, String paintMethod) {
		super(processData, departmentId);
		this.paintType = paintType;
		this.paintMethod = paintMethod;
	}

	public String getPaintType() {
		return paintType;
	}

	public void setPaintType(String paintType) {
		this.paintType = paintType;
	}

	public String getPaintMethod() {
		return paintMethod;
	}

	public void setPaintMethod(String paintMethod) {
		this.paintMethod = paintMethod;
	}

	@Override
	public String toString() {
		return "PaintProcess{" +
				"paintType='" + paintType + '\'' +
				", paintMethod='" + paintMethod + '\'' +
				", processId=" + processId +
				", processData='" + processData + '\'' +
				", departmentId=" + departmentId +
				'}';
	}
}
