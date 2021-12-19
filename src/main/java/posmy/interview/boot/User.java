package posmy.interview.boot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="userId")
	private int id;
	
	private String username;
	
	private boolean isDeleted;
	
	private int roleId;
	

	public User(int id, String username, boolean isDeleted, int roleId) {
		super();
		this.id = id;
		this.username = username;
		this.isDeleted = isDeleted;
		this.roleId = roleId;
	}

	public User(String username, boolean isDeleted, int roleId) {
		this.username = username;
		this.isDeleted = isDeleted;
		this.roleId = roleId;
	}

	public User() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", isDeleted=" + isDeleted + ", roleId=" + roleId + "]";
	}
}
