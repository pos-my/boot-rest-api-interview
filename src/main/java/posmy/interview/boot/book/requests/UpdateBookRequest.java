package posmy.interview.boot.book.requests;

import lombok.Data;

@Data
public class UpdateBookRequest {

    private String title;

    private String author;

    private String status;

    private String user;
}
