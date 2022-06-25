package posmy.interview.boot.exception;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import posmy.interview.boot.constant.ErrorEnum;
import posmy.interview.boot.constant.ErrorMessage;

/**
 * @author Hafiz
 * @version 0.01
 */
@ControllerAdvice
public class AppExceptionsHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<Object> handleAnyException(HttpServletRequest req, Exception ex){
		logger.error("handleAnyException!");
		
		String errorMsgDesc = ex.getLocalizedMessage();

		if(errorMsgDesc == null) errorMsgDesc = ex.toString();

		logger.error("errorMsgDesc:- "+errorMsgDesc);
		ex.printStackTrace();
		
		ErrorMessage errMsg = new ErrorMessage(new Date(), errorMsgDesc, "INTERNAL_SERVER_ERROR");
		
		return new ResponseEntity<>(errMsg, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(value = {WebserviceException.class})
	public ResponseEntity<Object> handleBillingException(WebserviceException tEx){
		
		HttpStatus httpStatus= null;
		String errorMsgDesc = tEx.getLocalizedMessage();
		String message = tEx.getMessage();

		if(errorMsgDesc == null) errorMsgDesc = tEx.toString();
		
		ErrorMessage errMsg = new ErrorMessage(new Date(), errorMsgDesc, tEx.getDetail());
		
		if(message.equalsIgnoreCase(ErrorEnum.UNAUTHORIZED_ACCESS.getDescription())) {
			httpStatus = HttpStatus.UNAUTHORIZED;
		}
		else if(message.equalsIgnoreCase(ErrorEnum.ACCESS_DENIED_INVALID_ENDPOINT.getDescription())) {
			httpStatus = HttpStatus.FORBIDDEN;
		}
		else if(message.equalsIgnoreCase(ErrorEnum.RESPONSE_RECEIVED_WITH_ERROR.getDescription())) {
			httpStatus = HttpStatus.BAD_REQUEST;
		}
		else if(message.equalsIgnoreCase(ErrorEnum.RECORD_NOT_FOUND.getDescription())) {
			httpStatus = HttpStatus.NOT_FOUND;
		}
		else{
			httpStatus = HttpStatus.BAD_REQUEST;
		}
		
		return new ResponseEntity<>(errMsg, new HttpHeaders(), httpStatus);
	}
}
