package posmy.interview.boot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import posmy.interview.boot.enums.BookState;

@Data
@SuperBuilder
@EqualsAndHashCode( callSuper = true )
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Immutable
@Table( name = "books" )
@JsonPropertyOrder( { "title", "author", "borrower", "status" } )
public class Book extends BaseObject {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "title", length = 20, nullable = false)
	@NotBlank(message = "title is mandatory")
	private String title;
	
	@Column(name = "author", length = 50, nullable = false)
	@NotBlank(message = "author is mandatory")
	private String author;
	
	@Column(name = "borrower", length = 50, nullable = true )
	private String borrower;
	
	@Enumerated( EnumType.STRING )
	@Column(name = "status", length = 20, nullable = false)
	@NotBlank(message = "status is mandatory")
	private BookState status;
	
}
