package posmy.interview.boot.model;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class CreateBookRequest {

    @NotNull
    private String title;
    @NotNull
    private String author;
}
