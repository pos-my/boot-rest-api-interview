package posmy.interview.boot.common.enums;

public enum BookStatusEnum {
    AVAILABLE("AVAILABLE"), BORROWED("BORROWED");

    private String bookStatus = null;

    private BookStatusEnum(String bookStatus) {
        this.bookStatus = bookStatus;
    }

    @Override
    public String toString() {
        return this.bookStatus;
    }
}
