package posmy.interview.boot.dto;

import lombok.Data;
import posmy.interview.boot.enums.BookStatus;

@Data
public class UpdateBookRecordDTO {
    private String title;

    private String description;

    private String author;

    private BookStatus bookStatus;
}
