package posmy.interview.boot.dto;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class CreateBookRecordDTO {
    @NotNull
    private String title;

    private String description;

    private String author;
}
