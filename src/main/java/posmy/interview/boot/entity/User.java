package posmy.interview.boot.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long Id;

	@Column
	private String username;

	@Column
	@JsonIgnore
	private String password;


	@Getter
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(	name = "user_roles", 
	joinColumns = @JoinColumn(name = "user_id"), 
	inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();


	@Getter @Setter
	@OneToMany ( mappedBy="users", cascade=CascadeType.MERGE, fetch=FetchType.EAGER, orphanRemoval=true )
	@JsonManagedReference
	private List<Book> book = new ArrayList<>();

	public User() {
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

}


