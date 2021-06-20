package posmy.interview.boot.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.ToString.Exclude;

@Data
@Entity
public class Book {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long isbn;
	
	private String title;
	
	public Status status;
	
	@Exclude
	@OneToMany(mappedBy = "book")
    private List<BookBorrowRecord> bookBorrowRecord;
	
	public enum Status {
		AVAILABLE, BORROWED
		;
	}
}
