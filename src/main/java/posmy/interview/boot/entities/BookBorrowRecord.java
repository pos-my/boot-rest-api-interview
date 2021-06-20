package posmy.interview.boot.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.ToString.Exclude;

@Data
@Entity
public class BookBorrowRecord {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Action action;
	
	public enum Action {
		BORROW, RETURN,
		;
	}
	
	@Exclude
	@ManyToOne
    @JoinColumn(name="book_isbn")
	private Book book;
	
	@Exclude
	@ManyToOne
    @JoinColumn(name="member_id")
	private Member member;
}
