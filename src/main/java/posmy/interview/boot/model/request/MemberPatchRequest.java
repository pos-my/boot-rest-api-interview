package posmy.interview.boot.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MemberPatchRequest {

    private String user;

    @NotBlank
    private String field;

    private String value;
}
