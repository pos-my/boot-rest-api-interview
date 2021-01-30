package posmy.interview.boot.model;

import lombok.Data;
import posmy.interview.boot.enums.Roles;

import javax.validation.constraints.NotBlank;

@Data
public class CreateUserRequest {

    @NotBlank(message = "username is mandatory")
    private String username;

    @NotBlank(message = "firstName is mandatory")
    private String firstName;

    @NotBlank(message = "lastName is mandatory")
    private String lastName;

    @NotBlank(message = "email is mandatory")
    private String email;

    @NotBlank(message = "password is mandatory")
    private String password;

    private Roles roles;

}
