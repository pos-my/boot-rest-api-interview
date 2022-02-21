package posmy.interview.boot.Model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;



@Data
public class AddBookReq {

    @NotNull(message = "Missing bookName in the request")
    @JsonProperty("bookName")
    private String bookName;

}
