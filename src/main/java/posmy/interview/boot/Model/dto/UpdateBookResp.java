package posmy.interview.boot.Model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateBookResp {
    @JsonProperty("responseId")
    private String responseId;

    @JsonProperty("bookId")
    private Long bookId;

    @JsonProperty("bookName")
    private String bookName;

    @JsonProperty("status")
    private String status;

}
