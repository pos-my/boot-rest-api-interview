package posmy.interview.boot.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import posmy.interview.boot.entity.Book;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookGetResponse implements BaseResponse {
    private Page<Book> page;
}
