package posmy.interview.boot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Book {
	
	enum Status {
		AVAILABLE,
		BORROWED
	}
	
	private @Id @GeneratedValue Long id;
	private String name;
	private Status status;
	
	Book(){
		
	}
	
	Book(String name){
		this.name = name;
		status = Status.AVAILABLE;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {		
		return name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Book {id=" + id + ", name='" + name + "', status='" + status + "'}";
	}
	
	

	
}
