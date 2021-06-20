package posmy.interview.boot.entities;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString.Exclude;

@Data
@Entity
public class Privilege {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@Exclude
	@ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;
	
	public Privilege() {
	}
	
	public Privilege(String name) {
		this.name = name;
	}

}
