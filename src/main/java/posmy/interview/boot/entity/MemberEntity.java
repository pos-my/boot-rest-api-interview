package posmy.interview.boot.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "MEMBER")
public class MemberEntity {

    @Id
    @Column(name = "MEMBER_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long memberId;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;

}
