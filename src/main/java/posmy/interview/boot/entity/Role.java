package posmy.interview.boot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import posmy.interview.boot.model.RoleConst;

@Entity
@Data
public class Role {


	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long Id;

	@Enumerated(EnumType.STRING)
	@Column
	private RoleConst name;

	@Column
	private String description;

}
