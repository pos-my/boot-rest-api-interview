package posmy.interview.boot.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import posmy.interview.boot.enums.BookStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookPutRequest {
    @NotBlank
    private String id;

    @NotBlank
    private String name;

    private String desc;

    private String imageUrl;

    @NotNull
    private BookStatus status;
}