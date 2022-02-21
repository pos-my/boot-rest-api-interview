package posmy.interview.boot.Model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
public class AddBookResp {

    @JsonProperty("bookId")
    private Long bookId;

    @JsonProperty("bookName")
    private String bookName;

    @JsonProperty("status")
    private String status;

}
