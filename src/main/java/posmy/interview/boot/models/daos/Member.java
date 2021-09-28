package posmy.interview.boot.models.daos;

import posmy.interview.boot.models.dtos.member.UserRole;

import javax.persistence.*;

@Entity
@Table(name = "MEMBERS")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public Member() {
    }

    public Member(String firstName, String lastName, String phone, String email,String password, UserRole userRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public long getMemberId() {
        return id;
    }

    public void setMemberId(long memberId) {
        this.id = memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "Member [memberId=" + id + ", firstName=" + firstName + ", lastName=" + lastName +
                ", phone=" + phone + ", email=" + email + ", userRole=" + userRole.name() + "]";
    }
}
