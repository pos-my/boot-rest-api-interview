package posmy.interview.boot.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.error.NoSuchBookException;
import posmy.interview.boot.model.request.BookPutRequest;
import posmy.interview.boot.repos.BookRepository;

@Service
public class BookPutService implements BaseService<BookPutRequest, Book> {

    private final BookRepository bookRepository;

    public BookPutService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book execute(BookPutRequest request) {
        Book existingBook = bookRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchBookException(request.getId()));
        BeanUtils.copyProperties(request, existingBook);
        return bookRepository.save(existingBook);
    }
}
