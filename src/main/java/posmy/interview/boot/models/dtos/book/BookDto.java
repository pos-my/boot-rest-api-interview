package posmy.interview.boot.models.dtos.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDto {

    private String bookId;

    @NotNull
    @NotEmpty
    @JsonProperty(value = "title", required = true)
    private String title;

    @NotNull
    @NotEmpty
    @JsonProperty(value = "author", required = true)
    private String author;

    @NotNull
    @NotEmpty
    @JsonProperty(value = "publishedYear", required = true)
    private String publishedYear;

    @NotNull
    @JsonProperty(value = "status", required = true)
    private BookStatus status;

    public BookDto() {}

    public BookDto(String bookId, String title, String author, String publishedYear, BookStatus status) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publishedYear = publishedYear;
        this.status = status;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(String publishedYear) {
        this.publishedYear = publishedYear;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}
