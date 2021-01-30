package posmy.interview.boot.model;

import lombok.Data;

@Data
public class BookRecordRequest {
    private long bookId;
    private long memberId;
}
