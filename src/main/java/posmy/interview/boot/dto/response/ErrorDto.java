package posmy.interview.boot.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto {

    private int responseCode;
    private String message;
}
