package posmy.interview.boot.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import posmy.interview.boot.exception.ErrorHandler;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


public class AuthUtil {

    private AuthUtil() {

    }

    @SneakyThrows
    public static void setAuthExceptionResponse(HttpServletResponse response, ObjectMapper mapper, String message) {
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getOutputStream().write(
                mapper.writeValueAsBytes(ErrorHandler.builder().error(message).build())
        )
        ;
    }
}
