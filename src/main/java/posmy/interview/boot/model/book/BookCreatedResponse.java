package posmy.interview.boot.model.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import posmy.interview.boot.model.common.BaseResponseWithPagination;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BookCreatedResponse {
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
