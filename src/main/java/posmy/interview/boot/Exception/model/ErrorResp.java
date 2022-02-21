package posmy.interview.boot.Exception.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResp extends BaseResp{
    public ErrorResp(String status, String message) {
        super(status, message);
    }

    public ErrorResp(String message) {
        this.setMessage(message);
    }
}
