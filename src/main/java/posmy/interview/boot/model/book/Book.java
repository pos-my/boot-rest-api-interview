package posmy.interview.boot.model.book;

public class Book {
    int bookId;
    String name;
    String description;
    String status;
    String recordCreateDate;
    String recordUpdateDate;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecordCreateDate() {
        return recordCreateDate;
    }

    public void setRecordCreateDate(String recordCreateDate) {
        this.recordCreateDate = recordCreateDate;
    }

    public String getRecordUpdateDate() {
        return recordUpdateDate;
    }

    public void setRecordUpdateDate(String recordUpdateDate) {
        this.recordUpdateDate = recordUpdateDate;
    }
}
