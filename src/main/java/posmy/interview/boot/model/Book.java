package posmy.interview.boot.model;

import java.io.Serializable;

import javax.persistence.*;

import lombok.*;
import posmy.interview.boot.dto.BookDto;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="book")
public class Book implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="bookId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookId;

    @Column(unique = true)
    private String isbn;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "available")
    private String available;
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId" )
	private User user;

	@Override
	public String toString() {
		return "Book [bookId=" + bookId + ", isbn=" + isbn + ", title=" + title + ", available=" + available + ", user="
				+ (user==null?"":user.getUserid()) + "]";
	}

	public Book(long bookId, String isbn, String title, String available) {
		this.bookId=bookId;
		this.isbn = isbn;
		this.title=title;
		this.available=available;
	}
	
	public Book(BookDto bookDto) {
		this.bookId=bookDto.getBookId();
		this.isbn = bookDto.getIsbn();
		this.title=bookDto.getTitle();
		this.available=bookDto.getAvailable();
	}
	
	

    
}