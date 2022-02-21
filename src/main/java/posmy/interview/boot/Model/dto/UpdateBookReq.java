package posmy.interview.boot.Model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@Builder
public class UpdateBookReq {

    @JsonProperty("bookName")
    private String bookName;

    @JsonProperty("borrower")
    private Long borrower;

    @Pattern(regexp = "(?:^AVAILABLE|^BORROWED$)", message = "Only AVAILABLE or BORROWED allow for this value")
    @JsonProperty("status")
    private String status;

}
