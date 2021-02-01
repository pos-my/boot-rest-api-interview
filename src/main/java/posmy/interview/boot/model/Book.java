package posmy.interview.boot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private String bookId;
    private String isbn;
    private String title;
    private String author;
    private String status;
}
