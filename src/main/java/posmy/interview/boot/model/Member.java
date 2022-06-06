package posmy.interview.boot.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

@Entity
@Data
public class Member extends BaseUser {
    private String firstName;
    private String lastName;

    //we can hash the password to hide the value
    private String password;
}
