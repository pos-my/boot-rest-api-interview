package posmy.interview.boot.entity;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Data
@SuperBuilder
@EqualsAndHashCode( onlyExplicitlyIncluded = true )
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BaseObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @EqualsAndHashCode.Include
    private Long id;

}
