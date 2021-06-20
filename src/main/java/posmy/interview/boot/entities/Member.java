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
public class Member {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String username;
	private String password;
	
	@Exclude
	@OneToMany(mappedBy = "member")
    private List<BookBorrowRecord> bookBorrowRecord;
}
