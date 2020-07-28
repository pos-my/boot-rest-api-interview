package posmy.interview.boot.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "name")
	private RoleName name;
	
	public enum RoleName {
		LIBRARIAN("Librarian"),
		MEMBER("Member"),
		ADMIN("ADMIN"),
		TEST("TEST");
		
		private String displayValue;
		
		private RoleName(String displayValue) {
			this.displayValue = displayValue;
		}
	}

}
