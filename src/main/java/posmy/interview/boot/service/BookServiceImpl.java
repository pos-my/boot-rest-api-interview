package posmy.interview.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.model.*;
import posmy.interview.boot.repository.BooksRepository;
import posmy.interview.boot.repository.BorrowActivityRepository;

import java.util.Optional;

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
        Optional<Books> optionalBooks = booksRepository.findById(borrowRequest.getBookId());
        if(optionalBooks.isPresent()) {
            Books books = optionalBooks.get();
            if (BookStatus.AVAILABLE.equals(books.getStatus())) {
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
        } else {
            return null;
        }
    }

    @Override
    public void returnBook(BorrowRequest borrowRequest) {
        BorrowActivity currentBorrowActivity =
                borrowActivityRepository.findByUserIdAndBookIdAndStatus(
                        borrowRequest.getUserId(), borrowRequest.getBookId(), BorrowStatus.ACTIVE
                );
        Optional<Books> optionalBorrowedBook = booksRepository.findById(borrowRequest.getBookId());
        if(optionalBorrowedBook.isPresent()) {
            Books borrowedBook = optionalBorrowedBook.get();
            borrowedBook.setStatus(BookStatus.AVAILABLE);
            currentBorrowActivity.setStatus(BorrowStatus.SUBMITTED);
            borrowActivityRepository.save(currentBorrowActivity);
            booksRepository.save(borrowedBook);
        }
    }
}
