package posmy.interview.boot.view;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Hafiz
 * @version 0.01
 */
public class LibrarianView {

    @JsonIgnore
    private UUID id;

    @NotNull(message = "Fullname cannot be null")
	private String fullname;

    @NotNull(message = "Librarian Id cannot be null")
	private String librarianId;

    @NotNull(message = "Salary cannot be null")
	private BigDecimal salary;

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getLibrarianId() {
		return librarianId;
	}

	public void setLibrarianId(String librarianId) {
		this.librarianId = librarianId;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

}
