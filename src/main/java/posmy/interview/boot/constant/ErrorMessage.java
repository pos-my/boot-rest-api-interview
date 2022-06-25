package posmy.interview.boot.constant;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Hafiz
 * @version 0.01
 */
public class ErrorMessage {

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date timestamp;
	private String error;
	private String error_description;
	
	public ErrorMessage() {
	}
	
	public ErrorMessage(Date timestamp, String error, String error_description) {
		super();
		this.timestamp = timestamp;
		this.error = error;
		this.error_description = error_description;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}
}
