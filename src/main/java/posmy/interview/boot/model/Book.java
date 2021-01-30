package posmy.interview.boot.model;

import lombok.Data;

@Data
public class Book {
    private String isbn;
    private String title;
    private String author;
    private String status;
}
