package posmy.interview.boot.entities.dto;

import lombok.Data;

@Data
public class BookDto {

    private String id;

    private String title;

    private String author;

    private String status;

    private String user;

    private String isnb;
}