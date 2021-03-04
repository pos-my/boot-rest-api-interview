package posmy.interview.boot.service;

import org.springframework.stereotype.Service;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.BorrowRecord;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.error.BookUnavailableException;
import posmy.interview.boot.model.request.BookBorrowRequest;
import posmy.interview.boot.model.response.EmptyResponse;
import posmy.interview.boot.repos.BookRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookBorrowService implements BaseService<BookBorrowRequest, EmptyResponse> {

    private final BookRepository bookRepository;

    public BookBorrowService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public EmptyResponse execute(BookBorrowRequest request) {
        Book book = validateBook(request.getBookId());
        populateBookBorrowRecords(book, request.getUsername());
        book.setStatus(BookStatus.BORROWED);
        bookRepository.save(book);
        return new EmptyResponse();
    }

    private Book validateBook(String id) {
        Book book = bookRepository.findById(id).orElseThrow();
        if (book.getStatus().equals(BookStatus.BORROWED))
            throw new BookUnavailableException(id);
        return book;
    }

    private void populateBookBorrowRecords(Book book, String username) {
        List<BorrowRecord> existingRecords = book.getBorrowRecords();
        if (existingRecords == null)
            existingRecords = new ArrayList<>();
        existingRecords.add(0, newRecord(username));
        book.setBorrowRecords(existingRecords);
    }

    private BorrowRecord newRecord(String username) {
        return BorrowRecord.builder()
                .username(username)
                .build();
    }
}
