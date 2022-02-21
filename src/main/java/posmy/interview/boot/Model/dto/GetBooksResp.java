package posmy.interview.boot.Model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import posmy.interview.boot.Model.Book;

import java.util.List;

@Builder
@Data
public class GetBooksResp {

    @JsonProperty("books")
    private List<Book> books;

}
