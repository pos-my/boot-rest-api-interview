package posmy.interview.boot.controller.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class AddBooksRequest {

    @NotEmpty(message = "Title cannot be blank")
    private List<String> titleList;
}
