package posmy.interview.boot.testutils.factories;

import java.util.ArrayList;
import java.util.List;
import posmy.interview.boot.dto.request.CreateBookDto;
import posmy.interview.boot.dto.request.UpdateBookDto;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.enums.BookStatus;

public final class BookFactory {

    private static BookFactory instance;

    private BookFactory() {

    }

    public static BookFactory getInstance() {
        if (instance == null) {
            instance = new BookFactory();
        }
        return instance;
    }

    public Book constructBook(BookStatus status) {
        return Book.builder()
            .id(1L)
            .title("title")
            .author("author")
            .status(status)
            .build();
    }

    public List<Book> constructListOfBook(BookStatus status) {
        List<Book> books = new ArrayList<>();
        books.add(constructBook(status));
        return books;
    }

    public CreateBookDto constructCreateBookDto() {
        return CreateBookDto.builder()
            .title("New title")
            .author("New author")
            .build();
    }

    public UpdateBookDto constructUpdateBookDto() {
        return UpdateBookDto.builder()
            .title("New title")
            .author("Author 1")
            .status(BookStatus.BORROWED)
            .build();
    }

}
