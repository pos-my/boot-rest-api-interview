package posmy.interview.boot.service;

import org.springframework.stereotype.Service;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.model.request.BookAddRequest;
import posmy.interview.boot.repos.BookRepository;

import java.util.UUID;

@Service
public class BookAddService implements BaseService<BookAddRequest, Book> {

    private final BookRepository bookRepository;

    public BookAddService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book execute(BookAddRequest request) {
        return bookRepository.save(Book.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .desc(request.getDesc())
                .imageUrl(request.getImageUrl())
                .status(BookStatus.AVAILABLE)
                .build());
    }
}
