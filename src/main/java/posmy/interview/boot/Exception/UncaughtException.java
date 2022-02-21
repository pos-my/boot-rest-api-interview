package posmy.interview.boot.Exception;


import lombok.Data;
import org.slf4j.MDC;

import static posmy.interview.boot.Constant.MDC_TRANS_ID;

@Data
public class UncaughtException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    public UncaughtException(String status, String errorMessage) {
        super(errorMessage);
        this.errorCode = status;
        this.errorMessage = errorMessage;
    }

    public UncaughtException(String status, String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
        this.errorCode = status;
        this.errorMessage = errorMessage;
    }
}
