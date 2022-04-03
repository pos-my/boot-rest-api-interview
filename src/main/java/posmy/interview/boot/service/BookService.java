package posmy.interview.boot.service;

import posmy.interview.boot.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> findAll();
    Optional<Book> update (Long id, String title);
}
