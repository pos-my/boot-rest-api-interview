package posmy.interview.boot.service;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import posmy.interview.boot.model.request.BookDeleteRequest;
import posmy.interview.boot.model.response.EmptyResponse;
import posmy.interview.boot.repos.BookRepository;

@Slf4j
@Service
public class BookDeleteService implements BaseService<BookDeleteRequest, EmptyResponse> {

    private final BookRepository bookRepository;

    public BookDeleteService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public EmptyResponse execute(BookDeleteRequest request) {
        String deleteId = request.getId();
        Try.run(() -> bookRepository.deleteById(deleteId))
                .recover(EmptyResultDataAccessException.class,
                        ex -> warnAndSuppress(ex, deleteId))
                .get();
        return new EmptyResponse();
    }

    private Void warnAndSuppress(Throwable ex, String id) {
        log.warn("Trying to delete non-existing book with ID: " + id, ex);
        return null;
    }
}
