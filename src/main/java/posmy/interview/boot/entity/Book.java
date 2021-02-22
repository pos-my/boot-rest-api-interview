package posmy.interview.boot.entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(	
		uniqueConstraints = { 
				@UniqueConstraint(columnNames = "bookCode"),
				@UniqueConstraint(columnNames = "bookName") 
		})
public class Book {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long Id;

	@Column
	private String bookCode;

	@Column	
	private String bookName;

	@Column
	private String status;

	@Column
	private LocalDate dateBorrowed;

	@Column
	private LocalDate dateReturned;

	@Getter @Setter
	@ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.MERGE)
	@JoinColumn(name = "User_Id", referencedColumnName = "Id")
	@JsonBackReference
	private User users;

	public Book() {
	}

	public Book(String bookCode, String bookName) {
		this.bookCode = bookCode;
		this.bookName = bookName;
	}

	@Override
	public boolean equals(Object v) {
		boolean retVal = false;

		if (v instanceof Book){
			Book ptr = (Book) v;
			retVal = ptr.Id == this.Id;
		}

		return retVal;
	}

}
