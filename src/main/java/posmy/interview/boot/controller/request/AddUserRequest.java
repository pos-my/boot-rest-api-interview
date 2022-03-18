package posmy.interview.boot.controller.request;

import lombok.Data;
import posmy.interview.boot.enums.UserRole;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddUserRequest {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotNull(message = "User role cannot be blank")
    private UserRole role;
}
