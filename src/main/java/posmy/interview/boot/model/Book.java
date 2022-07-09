package posmy.interview.boot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Book {

	@Id
	@Column(name = "book_id")
    private Long bookId;
	
	@Column(name = "book_name")
	private String bookName;
	
	@Column(name = "status")
	private String status;
	
	
}
