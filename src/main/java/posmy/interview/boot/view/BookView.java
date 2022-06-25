package posmy.interview.boot.view;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Hafiz
 * @version 0.01
 */
public class BookView {

    @JsonIgnore
    private UUID id;

    @NotNull(message = "Book Code cannot be null")
	private String bookCode;

    @NotNull(message = "Title cannot be null")
	private String title;

    @NotNull(message = "Author cannot be null")
	private String author;

	public String getBookCode() {
		return bookCode;
	}

	public void setBookCode(String bookCode) {
		this.bookCode = bookCode;
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

}
