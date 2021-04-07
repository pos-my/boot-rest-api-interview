package posmy.interview.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import posmy.interview.boot.system.Constant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookDto extends BaseDto {
    private String id;
    private String name;
    private Constant.BookState status;
}
