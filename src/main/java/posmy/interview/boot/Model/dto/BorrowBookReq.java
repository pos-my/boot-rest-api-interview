package posmy.interview.boot.Model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class BorrowBookReq {

    @JsonProperty("memberId")
    private Long memberId;

}
