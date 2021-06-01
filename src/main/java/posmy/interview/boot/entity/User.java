package posmy.interview.boot.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import posmy.interview.boot.enums.UserState;

@Data
@SuperBuilder
@EqualsAndHashCode( callSuper = true )
@Entity
@Table( name = "users" )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseObject {

	private static final long serialVersionUID = 1L;

	@Column(name = "username", length = 20, nullable = false)
	@NotBlank(message = "username is mandatory")
	private String username;
	
	@Column(name = "password", length = 100, nullable = false)
	@NotBlank(message = "password is mandatory")
	private String password;
	
	@Column(name = "firstname", length = 100, nullable = true )
	private String firstname;
	
	@Column(name = "lastname", length = 100, nullable = true )
	private String lastname;
	
	@Enumerated( EnumType.STRING )
	@Column(name = "status", length = 20, nullable = false)
	@NotNull(message = "status is mandatory")
	private UserState status;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
}
