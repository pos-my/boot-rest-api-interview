package posmy.interview.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.model.*;
import posmy.interview.boot.repository.BooksRepository;
import posmy.interview.boot.repository.BorrowActivityRepository;

@Service
public class BookServiceImpl implements BookService {

    private final BooksRepository booksRepository;

    private final BorrowActivityRepository borrowActivityRepository;

    @Autowired
    public BookServiceImpl(BooksRepository booksRepository,
                           BorrowActivityRepository borrowActivityRepository) {
        this.booksRepository = booksRepository;
        this.borrowActivityRepository = borrowActivityRepository;
    }

    @Override
    public BorrowActivity borrowBook(BorrowRequest borrowRequest) throws IllegalAccessException {
        Books books = booksRepository.getReferenceById(borrowRequest.getBookId());
        if(BookStatus.AVAILABLE.equals(books.getStatus())) {
            BorrowActivity currentBorrowing = borrowActivityRepository.findByUserIdAndBookIdAndStatus(
                    borrowRequest.getUserId(), borrowRequest.getBookId(), BorrowStatus.ACTIVE);

            // only update the status if the the user is not yet borrowing the book
            if (currentBorrowing == null) {
                books.setStatus(BookStatus.BORROWED);
                booksRepository.save(books);
                BorrowActivity borrowActivity = new BorrowActivity();
                borrowActivity.setUserId(borrowRequest.getUserId());
                borrowActivity.setBookId(borrowRequest.getBookId());
                borrowActivity.setStatus(BorrowStatus.ACTIVE);
                return borrowActivityRepository.save(borrowActivity);
            }

            return currentBorrowing;
        } else {
            throw new IllegalAccessException("Book is not available");
        }
    }

    @Override
    public void returnBook(BorrowRequest borrowRequest) {
        BorrowActivity currentBorrowActivity =
                borrowActivityRepository.findByUserIdAndBookIdAndStatus(
                        borrowRequest.getUserId(), borrowRequest.getBookId(), BorrowStatus.ACTIVE
                );
        Books borrowedBook = booksRepository.getReferenceById(borrowRequest.getBookId());
        borrowedBook.setStatus(BookStatus.AVAILABLE);
        currentBorrowActivity.setStatus(BorrowStatus.SUBMITTED);
        borrowActivityRepository.save(currentBorrowActivity);
        booksRepository.save(borrowedBook);
    }
}
