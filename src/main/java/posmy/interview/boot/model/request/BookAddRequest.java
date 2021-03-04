package posmy.interview.boot.model.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookAddRequest implements BaseRequest {
    @NotBlank
    private String name;

    private String desc;

    private String imageUrl;
}
