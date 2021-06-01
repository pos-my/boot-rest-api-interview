package posmy.interview.boot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import posmy.interview.boot.enums.UserRole;

@Data
@SuperBuilder
@EqualsAndHashCode( callSuper = true )
@Entity
@Table( name = "roles" )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseObject {

	private static final long serialVersionUID = 1L;

	@Enumerated( EnumType.STRING )
	@Column(name = "role", length = 20, nullable = false)
	@NotBlank(message = "role is mandatory")
	@NotNull(message = "role is mandatory")
	private UserRole role;
	
}
