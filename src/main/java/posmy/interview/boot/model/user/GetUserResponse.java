package posmy.interview.boot.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import posmy.interview.boot.model.book.Book;
import posmy.interview.boot.model.common.BaseResponseWithPagination;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GetUserResponse extends BaseResponseWithPagination {
    private List<User> records = new ArrayList<>();

    public List<User> getRecords() {
        return records;
    }

    public void setRecords(List<User> records) {
        this.records = records;
    }
}
