package posmy.interview.boot.dto.request;

import lombok.Builder;
import lombok.Data;
import posmy.interview.boot.enums.BookStatus;

@Data
@Builder
public class UpdateBookDto {

    private String title;
    private String author;
    private BookStatus status;
}
