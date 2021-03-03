package posmy.interview.boot.model.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.NoArgsConstructor;

@JsonSerialize
@NoArgsConstructor
public class EmptyResponse implements BaseResponse {
}
