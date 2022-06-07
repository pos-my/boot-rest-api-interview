package posmy.interview.boot.service;

import posmy.interview.boot.model.BorrowActivity;
import posmy.interview.boot.model.BorrowRequest;

public interface BookService {
    public BorrowActivity borrowBook(BorrowRequest borrowRequest) throws IllegalAccessException;
    public void returnBook(BorrowRequest borrowRequest);
}
