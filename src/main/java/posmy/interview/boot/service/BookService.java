package posmy.interview.boot.service;

import posmy.interview.boot.dto.BookDto;

import java.util.List;

public interface BookService {

    List<BookDto> findAll();

    BookDto findById(String id);

    BookDto createBook(BookDto bookDto);

    BookDto updateBook(BookDto bookDto, String id);

    void deleteBook(String id);
}
