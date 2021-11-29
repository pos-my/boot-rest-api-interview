package posmy.interview.boot.constant;

public interface Constant {
    int PAGE_INDEX = 20;
    int MIN_PAGE = 0;
    String PAGE_ERROR = "Page number must not be negative values";
    String USER_ERROR_USER_EXIST = "User has already exists";
    String USER_ERROR_USER_NOT_EXIST = "User does not exists";
    String USER_ERROR_LIB_NOT_ALL_DEL = "Librarian is not allowed to be deleted";
    String USER_ERROR_USER_IS_INACTIVE = "User is already inactive";
    String USER_ERROR_HAS_BOOK = "User still has borrowed book, please return before deactivating";

    String BOOK_ERROR_BOOK_EXIST = "Book has already exists";
    String BOOK_ERROR_BOOK_NOT_EXIST = "Book does not exists";
    String BOOK_ERROR_BOOK_BORROWED = "Ensure that book is returned, before deleting";
    String BOOK_ERROR_BOOK_IS_BORROWED = "Someone else has already borrowed the book";
    String BOOK_ERROR_BOOK_IS_NOT_BORROWED = "Book is still available, unable to return";
    String BOOK_ERROR_BORROWER_DOES_NOT_MATCH = "Borrower does not match, unable to return";

    enum UserStatus {
        ACTIVE,
        INACTIVE
    }

    enum BookStatus {
        BORROWED,
        AVAILABLE,
    }

    enum UserRole {
        MEMBER,
        LIBRARIAN
    }
}
