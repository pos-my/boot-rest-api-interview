package posmy.interview.boot.service;

import posmy.interview.boot.dto.BookDto;

import java.util.List;

public interface BookService {

    List<BookDto> findAll();
}
