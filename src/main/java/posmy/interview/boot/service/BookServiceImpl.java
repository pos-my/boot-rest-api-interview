package posmy.interview.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.repo.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAll() {

        Iterable<Book> books = bookRepository.findAll();
        List<Book> bookList = StreamSupport
                .stream(books.spliterator(), false)
                .collect(Collectors.toList());

        return bookList;
    }

    public Optional<Book> update(Long id, String title) {
//        Optional<Book> book = bookRepository.findById(id);

        Book book = new Book(id,title);
        bookRepository.save(book);
        return bookRepository.findById(id);
    }
}
