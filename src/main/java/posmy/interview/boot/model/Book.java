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
@Table(name = "books")
public class Book implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "title")
	private String title;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private BookStatus status;
	
	public enum BookStatus {
		AVAILABLE("Available"),
		BORROWED("Borrowed");
		
		private String displayValue;
		
		private BookStatus(String displayValue) {
			this.displayValue = displayValue;
		}
	}
}