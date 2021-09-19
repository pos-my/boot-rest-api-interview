package posmy.interview.boot.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import lombok.*;
import posmy.interview.boot.dto.UserDto;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
//@ToString(callSuper=true, exclude="password")
@Entity
@Table(name="User")
public class User implements Serializable {
    
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="userid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userid;
	
	@Column(name="username",unique=true)
	private String username;
	
	@Column(name="password")
	private String password;

	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;
    
	@Column(name="accStatus")
	@Enumerated(EnumType.STRING)
	private AccStatus accStatus;
    
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
	private Set<Book> book = new HashSet<>();

	@Override
	public String toString() {
		return "User [userid=" + userid + ", username=" + username + ", password=" + password + ", role=" + role
				+ ", accStatus=" + accStatus + "]";
	}
	
	public User(UserDto userDto) {
		this.userid = userDto.getUserid();
		this.password = userDto.getPassword();
		this.role = Role.valueOf(userDto.getRole())==null?null:Role.valueOf(userDto.getRole());
		this.accStatus = AccStatus.valueOf(userDto.getAccStatus())==null?null:AccStatus.valueOf(userDto.getAccStatus());
		this.username = userDto.getUsername();	
	}
	
    
}