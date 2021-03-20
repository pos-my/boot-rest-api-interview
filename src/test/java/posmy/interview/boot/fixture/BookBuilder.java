package posmy.interview.boot.fixture;

import posmy.interview.boot.dto.BookCreateDto;
import posmy.interview.boot.dto.BookUpdateDto;
import posmy.interview.boot.dto.IDto;
import posmy.interview.boot.dto.IUpdateDto;
import posmy.interview.boot.entity.Book;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class BookBuilder implements IBuilder<Book, Long> {

    private Long id;

    private String title = randomAlphabetic(10);

    public static BookBuilder sample() {
        return new BookBuilder();
    }

    @Override
    public Book build() {
        Book book = new Book();
        book.setTitle(title);
        return book;
    }

    @Override
    public IDto buildDto() {
        return null;
    }

    @Override
    public BookCreateDto buildCreateDto() {
        BookCreateDto dto = new BookCreateDto();
        dto.title = this.title;
        return dto;
    }

    @Override
    public BookUpdateDto buildUpdateDto() {
        BookUpdateDto dto = new BookUpdateDto();
        dto.title = this.title;
        return dto;
    }

    public BookBuilder setTitle(String title) {
        this.title = title;
        return this;
    }
}
