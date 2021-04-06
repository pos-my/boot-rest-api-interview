package posmy.interview.boot.dto;

import lombok.Data;
import posmy.interview.boot.system.Constant;

@Data
public class BookDto extends BaseDto {
    private String id;
    private String name;
    private Constant.BookState status;
}
