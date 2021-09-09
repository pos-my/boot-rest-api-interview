package posmy.interview.boot.enums;

public enum ErrorCode {

    BAD_REQUEST("BAD_REQUEST", "Invalid request"),
    BOOK_ALREADY_EXISTS("BOOK_ALREADY_EXISTS", "Book with same ISBN already exists"),
    BOOK_NOT_AVAILABLE("BOOK_NOT_AVAILABLE", "Book not available for borrowing"),
    BOOK_NOT_BORROWED_BY_USER("BOOK_NOT_BORROWED_BY_USER", "Book not able to be returned as it is not borrowed by user"),
    BOOK_NOT_FOUND("BOOK_NOT_FOUND", "Unable to find corresponding book"),
    EXCEPTION_FOUND("EXCEPTION_FOUND","Error exception encountered"),
    INVALID_ACCOUNT("INVALID_ACCOUNT", "User account not found"),
    INVALID_BOOK_STATUS("INVALID_BOOK_STATUS", "Invalid book status used"),
    PENDING_RETURN_BOOKS("PENDING_RETURN_BOOKS", "Member still has books yet to be returned"),
    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", "User ID already in use, please select another user ID"),
    USER_UNAUTHORIZED("USER_UNAUTHORIZED", "User account is not authorized");

    private final String code;
    private final String description;

    private ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}