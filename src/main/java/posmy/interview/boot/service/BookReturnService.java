package posmy.interview.boot.service;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.BorrowRecord;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.error.BookAlreadyReturnedException;
import posmy.interview.boot.error.BorrowRecordMismatchException;
import posmy.interview.boot.model.request.BookReturnRequest;
import posmy.interview.boot.model.response.EmptyResponse;
import posmy.interview.boot.repos.BookRepository;

import javax.transaction.Transactional;

@Service
public class BookReturnService implements BaseService<BookReturnRequest, EmptyResponse> {

    private final BookRepository bookRepository;

    public BookReturnService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public EmptyResponse execute(BookReturnRequest request) {
        Book book = validateBook(request.getBookId());
        updateBorrowRecord(book, request);
        book.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(book);
        return new EmptyResponse();
    }

    private Book validateBook(String id) {
        Book book = bookRepository.findById(id).orElseThrow();
        if (book.getStatus().equals(BookStatus.AVAILABLE))
            throw new BookAlreadyReturnedException(id);
        return book;
    }

    private void updateBorrowRecord(Book book, BookReturnRequest request) {
        BorrowRecord record = book.getBorrowRecords().stream()
                .sorted()   //DESC by borrowTimestamp
                .findFirst()
                .filter(it -> StringUtils.equals(it.getUsername(), request.getUsername()))
                .filter(it -> BooleanUtils.isFalse(it.getIsReturn()))
                .orElseThrow(() -> new BorrowRecordMismatchException(request.getBookId(), request.getUsername()));
        record.setIsReturn(true);
    }
}
