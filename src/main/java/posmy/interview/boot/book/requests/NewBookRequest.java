package posmy.interview.boot.book.requests;

import lombok.Data;

@Data
public class NewBookRequest {

    private String title;

    private String author;

    private String isnb;
}
