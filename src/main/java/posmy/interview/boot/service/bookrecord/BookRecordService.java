package posmy.interview.boot.service.bookrecord;

import posmy.interview.boot.exception.NoDataFoundException;
import posmy.interview.boot.model.BookRecordRequest;

public interface BookRecordService {
    long borrowBook(BookRecordRequest bookRecordRequest) throws NoDataFoundException;
    void returnBook(BookRecordRequest bookRecordRequest) throws NoDataFoundException;
}
