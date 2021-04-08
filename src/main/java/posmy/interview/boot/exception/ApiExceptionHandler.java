package posmy.interview.boot.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource errorMessageSource;

    @ExceptionHandler({BaseRuntimeException.class})
    public <T extends BaseRuntimeException> ResponseEntity<String> handleBaseRuntimeException(T ex) {
        log.error(ex.getMessage());

        String exceptionName = ex.getClass().getSimpleName();
        return new ResponseEntity<>(getErrorMessage(exceptionName, ex.getMessageParams()), getErrorHttpStatus(exceptionName));
    }

    private String getErrorMessage(String exceptionName, Object[] messageParams) {
        final String METHOD = getClass().getSimpleName() + ".getErrorMessage: ";
        try {
            return errorMessageSource.getMessage(exceptionName + ".message", messageParams, Locale.getDefault());
        } catch (NoSuchMessageException ex) {
            if (log.isWarnEnabled()) {
                log.warn(METHOD + exceptionName + ".message cannot be found in errors.properties file, reverting to default error message.");
            }
            return errorMessageSource.getMessage("Exception.message", messageParams, Locale.getDefault());
        }
    }

    private HttpStatus getErrorHttpStatus(String exceptionName) {
        final String METHOD = getClass().getSimpleName() + ".getErrorHttpStatus: ";
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        try {
            String statusCodeString = errorMessageSource.getMessage(exceptionName + ".code", null, Locale.getDefault());
            if (StringUtils.isNotEmpty(statusCodeString) && StringUtils.isNumeric(statusCodeString)) {
                statusCode = HttpStatus.valueOf(Integer.parseInt(statusCodeString));
            }
        } catch (NoSuchMessageException ex) {
            if (log.isWarnEnabled()) {
                log.warn(METHOD + exceptionName + ".message cannot be found in errors.properties file, reverting to default error message.");
            }
            // Default to 500 if statusCode not found
        }
        return statusCode;
    }
}
