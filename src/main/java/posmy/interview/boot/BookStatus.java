package posmy.interview.boot;

public enum BookStatus {

	AVAILABLE("AVAILABLE", "Available"),
	
	BORROWED("BORROWED", "Borrowed");
	
	private String value;
	
	private String label;
	
	BookStatus(String value, String label) {
		this.value = value;
		this.label = label;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
