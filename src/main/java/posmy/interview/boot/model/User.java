package posmy.interview.boot.model;

import lombok.Data;

@Data
public class User {

    private long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
