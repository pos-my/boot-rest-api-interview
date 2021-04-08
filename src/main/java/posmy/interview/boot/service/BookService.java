package posmy.interview.boot.service;

import posmy.interview.boot.dto.BookDto;

import java.util.List;

public interface BookService {

    List<BookDto> findAll();

    BookDto findById(String id);

    List<BookDto> findAvailableBooks();

    BookDto createBook(BookDto bookDto);

    BookDto updateBook(BookDto bookDto, String id);

    void deleteBook(String id);

    BookDto borrowBook(String loginId, String bookId);

    BookDto returnBook(String bookId);
}
