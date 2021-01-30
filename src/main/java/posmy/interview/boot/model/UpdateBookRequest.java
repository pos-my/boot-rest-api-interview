package posmy.interview.boot.model;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class UpdateBookRequest {
    @NotNull
    private long bookId;
    @NotNull
    private String title;
    @NotNull
    private String author;
}
