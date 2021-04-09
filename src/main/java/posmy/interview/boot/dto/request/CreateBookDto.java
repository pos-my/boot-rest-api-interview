package posmy.interview.boot.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBookDto {

    private String title;
    private String author;
}