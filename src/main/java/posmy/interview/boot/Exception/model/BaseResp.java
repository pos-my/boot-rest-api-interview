package posmy.interview.boot.Exception.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResp {

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

}
