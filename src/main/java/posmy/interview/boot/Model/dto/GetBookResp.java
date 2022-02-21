package posmy.interview.boot.Model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetBookResp {

    @JsonProperty("bookId")
    private Long bookId;

    @JsonProperty("bookName")
    private String bookName;

    @JsonProperty("borrower")
    private Long borrower;

    @JsonProperty("status")
    private String status;

}
