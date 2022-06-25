package posmy.interview.boot.exception;

import posmy.interview.boot.constant.ErrorEnum;

/**
 * @author Hafiz
 * @version 0.01
 */
public class WebserviceException extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ErrorEnum error;
    private String description;
    private String detail;
    private String message;

    public WebserviceException(String msg) {
        super(msg);
    }

    public WebserviceException(ErrorEnum error){
        this.description = error.getDescription();
        this.message = error.getDescription();
        this.detail = error.getDetail();
    }

    public <T> WebserviceException(ErrorEnum error, T t){
        this.description = error.getDescription();
        this.message = error.getDescription();
        this.detail = error.getDetail() + " - " + t;
    }

	public ErrorEnum getError() {
        return error;
    }

    public void setError(ErrorEnum error) {
        this.error = error;
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

    // Overrides Exception's getMessage()
    @Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
