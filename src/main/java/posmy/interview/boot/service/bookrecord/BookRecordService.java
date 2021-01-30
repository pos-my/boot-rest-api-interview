package posmy.interview.boot.service.bookrecord;

import posmy.interview.boot.exception.NoDataFoundException;

public interface BookRecordService {
    long borrowBook(long bookId) throws NoDataFoundException;
    void returnBook(long bookId) throws NoDataFoundException;
}
