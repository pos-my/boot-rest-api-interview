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
        Book book = bookRepository.findById(request.getBookId()).orElseThrow();
        if (book.getStatus().equals(BookStatus.AVAILABLE))
            throw new BookAlreadyReturnedException(request.getBookId());
        book.setStatus(BookStatus.AVAILABLE);
        BorrowRecord record = book.getBorrowRecords().stream()
                .sorted()
                .findFirst()
                .filter(it -> StringUtils.equals(it.getUsername(), request.getUsername()))
                .filter(it -> BooleanUtils.isFalse(it.getIsReturn()))
                .orElseThrow(() -> new BorrowRecordMismatchException(request.getBookId(), request.getUsername()));
        record.setIsReturn(true);
        bookRepository.save(book);
        return new EmptyResponse();
    }
}
