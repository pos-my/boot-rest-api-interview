package posmy.interview.boot.model.book;

import posmy.interview.boot.model.common.BaseResponseWithPagination;

import java.util.List;

public class BookResponse extends BaseResponseWithPagination {
    List<Book> records;

    public List<Book> getRecords() {
        return records;
    }

    public void setRecords(List<Book> records) {
        this.records = records;
    }
}
