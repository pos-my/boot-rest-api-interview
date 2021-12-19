package posmy.interview.boot;

public enum RoleType {

	LIBRARIAN(1, "Librarian"),
	
	MEMBER(2, "Member");
	
	private int value;
	
	private String label;
	
	RoleType(int value, String label) {
		this.value = value;
		this.label = label;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
